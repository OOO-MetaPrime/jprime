package mp.jprime.imex.csvwriter.services;

import mp.jprime.dataaccess.JPObjectRepositoryService;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.beans.JPObjectBase;
import mp.jprime.dataaccess.params.JPSelect;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.beans.JPAttrBean;
import mp.jprime.meta.beans.JPClassBean;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.parsers.ParserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import jakarta.annotation.PostConstruct;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@SpringBootTest
@ActiveProfiles("JpNsiCsvWriterBaseServiceTest")
@ContextConfiguration(classes = {JPObjectCsvWriterServiceTest.Config.class})
@Tag("integrationTests")
public class JPObjectCsvWriterServiceTest {
  @Lazy(false)
  @Profile("JpNsiCsvWriterBaseServiceTest")
  @Configuration
  @ComponentScan(
      basePackages = {"mp.jprime.imex.csvwriter.services"},
      lazyInit = true
  )
  @EnableConfigurationProperties
  public static class Config {
  }

  private static final String JPCLASS_ID_ATTR = "jpClassCodeAttr";
  private static final String JPCLASS_NAME_ATTR = "jpClassNameAttr";
  private static final String JPCLASS_CODE = "jpClass";
  private static final Collection<JPAttr> attrs = List.of(
      JPAttrBean.newBuilder()
          .code(JPCLASS_ID_ATTR)
          .identifier(true)
          .name("Код jpClass")
          .build(),
      JPAttrBean.newBuilder()
          .code(JPCLASS_NAME_ATTR)
          .name("Имя jpClass")
          .build()
  );
  private static final JPClass JPCLASS = JPClassBean.newBuilder()
      .code(JPCLASS_CODE)
      .attrs(attrs)
      .build();
  private static final long TOTAL_COUNT = 100L;
  private static final int OBJECTS_IN_FILE = 50;

  @Autowired
  private JPObjectCsvWriterCommonService service;
  @MockBean
  private JPObjectRepositoryService mockRepo;
  @MockBean
  private ParserService mockParser;
  @MockBean
  private JPMetaStorage mockMetaStorage;

  @PostConstruct
  void beforeAll() {
    service.setParserService(mockParser);
    service.setJpObjectRepositoryService(mockRepo);
    Mockito.when(mockParser.parseTo(Mockito.any(), Mockito.any())).thenAnswer(x -> x.getArgument(1));
    Mockito.when(mockParser.toString(Mockito.any())).thenAnswer(x -> x.getArgument(0));
    Mockito.when(mockMetaStorage.getJPClassByCode(Mockito.eq(JPCLASS_CODE))).thenReturn(JPCLASS);
  }

  @Test
  void zipTest() {
    Mockito.when(mockRepo.getTotalCount(Mockito.any(JPSelect.class))).thenReturn(TOTAL_COUNT);
    Mockito.when(mockRepo.getList(Mockito.any(JPSelect.class))).thenAnswer(x -> getObjects(((JPSelect) x.getArgument(0)).getOffset(), TOTAL_COUNT));

    try (InputStream inputStream = service.zipOf(JPCLASS.getCode(),
        attrs.stream().map(JPAttr::getCode).toList(),
        "fileName",
        OBJECTS_IN_FILE);
         ZipInputStream zipInputStream = new ZipInputStream(inputStream);
         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(zipInputStream))) {
      // Проверка наличия первого файла в архиве
      ZipEntry entry = zipInputStream.getNextEntry();
      Assertions.assertNotNull(entry);
      Assertions.assertEquals("fileName (1).csv", entry.getName());

      // Проверка первой строки с заголовками в первом файле
      String actualString = bufferedReader.readLine();
      Assertions.assertEquals("\"Код jpClass\",\"Имя jpClass\"", actualString);
      for (int i = 0; i < OBJECTS_IN_FILE; i++) {
        // Проверка всех строк со значениями
        actualString = bufferedReader.readLine();
        Assertions.assertEquals("\"jpClassCodeAttr" + i + "\",\"jpClassNameAttr" + i + "\"", actualString);
      }

      // Проверка, что после цикла в первом файле не осталось строк
      actualString = bufferedReader.readLine();
      Assertions.assertNull(actualString);

      // Проверка наличия второго файла
      entry = zipInputStream.getNextEntry();
      Assertions.assertNotNull(entry);
      Assertions.assertEquals("fileName (2).csv", entry.getName());

      // Проверка первой строки с заголовками во втором файле
      actualString = bufferedReader.readLine();
      Assertions.assertEquals("\"Код jpClass\",\"Имя jpClass\"", actualString);
      for (int i = OBJECTS_IN_FILE; i < TOTAL_COUNT; i++) {
        // Проверка всех строк со значениями
        actualString = bufferedReader.readLine();
        Assertions.assertEquals("\"jpClassCodeAttr" + i + "\",\"jpClassNameAttr" + i + "\"", actualString);
      }

      // Проверка, что во втором файле не осталось строк
      actualString = bufferedReader.readLine();
      Assertions.assertNull(actualString);

      // Проверка, что больше нет файлов
      entry = zipInputStream.getNextEntry();
      Assertions.assertNull(entry);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void oneFileTest() {
    Mockito.when(mockRepo.getTotalCount(Mockito.any(JPSelect.class))).thenReturn(TOTAL_COUNT);
    Mockito.when(mockRepo.getList(Mockito.any(JPSelect.class))).thenAnswer(x -> getObjects(((JPSelect) x.getArgument(0)).getOffset(), TOTAL_COUNT));

    try (InputStream inputStream = service.of(JPCLASS.getCode(),
        attrs.stream().map(JPAttr::getCode).toList(),
        "fileName");
         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
      // Проверка строки с заголовками
      String actualString = bufferedReader.readLine();
      Assertions.assertEquals("\"Код jpClass\",\"Имя jpClass\"", actualString);
      for (int i = 0; i < TOTAL_COUNT; i++) {
        // Проверка всех строк со значениями
        actualString = bufferedReader.readLine();
        Assertions.assertEquals("\"jpClassCodeAttr" + i + "\",\"jpClassNameAttr" + i + "\"", actualString);
      }

      // Проверка, что больше не осталось строк
      actualString = bufferedReader.readLine();
      Assertions.assertNull(actualString);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void oneValueFileTest() {
    Mockito.when(mockRepo.getTotalCount(Mockito.any(JPSelect.class))).thenReturn(1L);
    Mockito.when(mockRepo.getList(Mockito.any(JPSelect.class))).thenAnswer(x -> getObjects(((JPSelect) x.getArgument(0)).getOffset(), 1));

    try (InputStream inputStream = service.of(JPCLASS.getCode(),
        attrs.stream().map(JPAttr::getCode).toList(),
        "fileName");
         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
      // Проверка строки с заголовками
      String actualString = bufferedReader.readLine();
      Assertions.assertEquals("\"Код jpClass\",\"Имя jpClass\"", actualString);
      for (int i = 0; i < 1; i++) {
        // Проверка всех строк со значениями
        actualString = bufferedReader.readLine();
        Assertions.assertEquals("\"jpClassCodeAttr" + i + "\",\"jpClassNameAttr" + i + "\"", actualString);
      }

      // Проверка, что больше не осталось строк
      actualString = bufferedReader.readLine();
      Assertions.assertNull(actualString);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void emptyFileTest() {
    Mockito.when(mockRepo.getTotalCount(Mockito.any(JPSelect.class))).thenReturn(0L);
    Mockito.when(mockRepo.getList(Mockito.any(JPSelect.class))).thenAnswer(x -> getObjects(((JPSelect) x.getArgument(0)).getOffset(), 0));

    try (InputStream inputStream = service.of(JPCLASS.getCode(),
        attrs.stream().map(JPAttr::getCode).toList(),
        "fileName");
         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));) {
      // Проверка первой строки с заголовками в первом файле
      String actualString = bufferedReader.readLine();
      Assertions.assertEquals("\"Код jpClass\",\"Имя jpClass\"", actualString);

      // Проверка, что больше не осталось строк
      actualString = bufferedReader.readLine();
      Assertions.assertNull(actualString);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Генерирует jpObject не более, чем (totalCount - offset) или OBJECTS_IN_FILE, дополняя их значение порядковым номером, зависящим от offset
   */
  private Collection<JPObject> getObjects(int offset, long totalCount) {
    Collection<JPObject> result = new ArrayList<>();
    try {
      for (int i = offset; i < OBJECTS_IN_FILE + offset && i < totalCount; i++) {
        result.add(JPObjectBase.newBaseInstance(
            JPCLASS_CODE, JPCLASS_ID_ATTR, JPData.of(Map.of(JPCLASS_ID_ATTR, JPCLASS_ID_ATTR + i, JPCLASS_NAME_ATTR, JPCLASS_NAME_ATTR + i))
        ));
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return result;
  }
}
