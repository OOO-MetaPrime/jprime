package mp.jprime.imex.csvwriter.services;

import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.beans.JPObjectBase;
import mp.jprime.meta.beans.JPAttrBean;
import mp.jprime.parsers.ParserService;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class JPObjectCsvZipWriterTest {

  private static final Collection<JPObject> TEST_VALUES = List.of(
      JPObjectBase.newBaseInstance(
          "jpClassCode",
          "jpClassId",
          JPData.of(Map.of("firstCode", "Id 1", "secondCode", "Второе значение 1"))
      ),
      JPObjectBase.newBaseInstance(
          "jpClassCode",
          "jpClassId",
          JPData.of(Map.of("firstCode", "Id 2", "secondCode", "Второе значение 2"))
      ),
      JPObjectBase.newBaseInstance(
          "jpClassCode",
          "jpClassId",
          JPData.of(Map.of("firstCode", "Id 3", "secondCode", "Второе значение 3"))
      )
  );
  private static final String EXPECTED_VALUE_TEMPLATE = "\"Id %1$d\",\"Второе значение %1$d\"\n";
  private static final String EXPECTED_FILE_NAME_TEMPLATE = "fileName (%d).csv";
  private static final String EXPECTED_HEADER = "\"Имя первого\",\"Второе имя\"\n";
  private static final String EXPECTED_RESULT = EXPECTED_HEADER +
      String.format(EXPECTED_VALUE_TEMPLATE, 1) +
      String.format(EXPECTED_VALUE_TEMPLATE, 2) +
      String.format(EXPECTED_VALUE_TEMPLATE, 3);

  @Test
  void shouldWriteAllLines() {
    ParserService mockParserService = Mockito.mock(ParserService.class);
    Mockito.when(mockParserService.toString(Mockito.any())).thenAnswer(x -> x.getArgument(0));

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (JPObjectCsvZipWriter writer = JPObjectCsvZipWriter.newBuilder(
            new ZipOutputStream(baos),
            mockParserService,
            List.of(
                JPAttrBean.newBuilder().code("firstCode").name("Имя первого").build(),
                JPAttrBean.newBuilder().code("secondCode").name("Второе имя").build()
            ),
            "fileName")
        .build()
    ) {
      CompletableFuture.runAsync(() -> writer.write(TEST_VALUES)).join();
    }

    try (ZipInputStream actual = new ZipInputStream(new ByteArrayInputStream(baos.toByteArray()));) {
      ZipEntry actualEntry = actual.getNextEntry();
      Assertions.assertNotNull(actualEntry);
      Assertions.assertEquals("fileName (1).csv", actualEntry.getName());
      Assertions.assertEquals(EXPECTED_RESULT, new String(actual.readAllBytes(), StandardCharsets.UTF_8));
      actualEntry = actual.getNextEntry();
      Assertions.assertNull(actualEntry);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void shouldWriteEachLineInNewFile() {
    ParserService mockParserService = Mockito.mock(ParserService.class);
    Mockito.when(mockParserService.toString(Mockito.any())).thenAnswer(x -> x.getArgument(0));

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (JPObjectCsvZipWriter writer = JPObjectCsvZipWriter.newBuilder(
            new ZipOutputStream(baos),
            mockParserService,
            List.of(
                JPAttrBean.newBuilder().code("firstCode").name("Имя первого").build(),
                JPAttrBean.newBuilder().code("secondCode").name("Второе имя").build()
            ),
            "fileName")
        .maxFileSize(1)
        .build()
    ) {
      CompletableFuture.runAsync(() -> writer.write(TEST_VALUES)).join();
    }

    try (ZipInputStream actual = new ZipInputStream(new ByteArrayInputStream(baos.toByteArray()));) {
      ZipEntry actualEntry = actual.getNextEntry();
      Assertions.assertNotNull(actualEntry);
      int counter = 1;
      while (actualEntry != null) {
        Assertions.assertEquals(String.format(EXPECTED_FILE_NAME_TEMPLATE, counter), actualEntry.getName());
        Assertions.assertEquals(String.format(EXPECTED_HEADER + EXPECTED_VALUE_TEMPLATE, counter++), new String(actual.readAllBytes(), StandardCharsets.UTF_8));

        actualEntry = actual.getNextEntry();
      }

      actualEntry = actual.getNextEntry();
      Assertions.assertNull(actualEntry);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
