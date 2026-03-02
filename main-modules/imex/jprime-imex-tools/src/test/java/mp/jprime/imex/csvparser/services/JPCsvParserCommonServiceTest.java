package mp.jprime.imex.csvparser.services;

import mp.jprime.exceptions.JPAppRuntimeException;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.imex.csvparser.beans.JPCsvReaderSettingsBean;
import mp.jprime.imex.rules.JPMapRules;
import mp.jprime.imex.rules.beans.JPMapRulesIndexedBean;
import mp.jprime.imex.rules.beans.JPMapRulesNamedBean;
import mp.jprime.lang.JPMap;
import mp.jprime.meta.beans.JPType;
import mp.jprime.parsers.ParserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;

import jakarta.annotation.PostConstruct;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collection;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = JPCsvParserCommonService.class)
@ActiveProfiles("csvparsertest")
public class JPCsvParserCommonServiceTest {

  @Autowired
  private JPCsvParserCommonService jpCsvParserService;

  @MockitoBean
  private ParserService mockParserService;

  @Value("classpath:csvparser/withHeadersWithQuoteWithNoMandatoryValues.csv")
  private Resource withHeadersWithQuoteWithNoMandatoryValues;

  @Value("classpath:csvparser/notParsableValues.csv")
  private Resource notParsableValues;

  @Value("classpath:csvparser/onlyHeaders.csv")
  private Resource onlyHeaders;

  @Value("classpath:csvparser/noHeadersWithoutQuote.csv")
  private Resource noHeadersWithoutQuote;

  @Value("classpath:csvparser/withHeadersWithoutQuote.csv")
  private Resource withHeadersWithoutQuote;

  @Value("classpath:csvparser/withHeadersWithQuoteWithBlankMandatoryValues.csv")
  private Resource withHeadersWithQuoteWithBlankMandatoryValues;

  @Value("classpath:csvparser/withHeadersWithQuoteWithEmptyMandatoryValues.csv")
  private Resource withHeadersWithQuoteWithEmptyMandatoryValues;

  private final static String COLUMN_CODE = "CODE";

  private final static String COLUMN_NAME = "NAME";

  private final static String COLUMN_DATE = "DATE";

  private final static String ATTR_CODE = "code";

  private final static String ATTR_CODE_COPY = "code_copy";

  private final static String ATTR_NAME = "name";

  private final static String ATTR_DATE = "date";

  private final static String CODE = "620-001";

  private final static String NAME = "ТЕРРИТОРИАЛЬНЫЙ ПУНКТ В Р.П.ЕРМИШЬ МЕЖРАЙОННОГО ОТДЕЛЕНИЯ УФМС РОССИИ ПО РЯЗАНСКОЙ ОБЛАСТИ В Г.САСОВО";

  private final static LocalDate DATE = LocalDate.of(2023, 11, 11);

  JPMapRules<String> RULES_MANY_MAP = JPMapRulesNamedBean.newBuilder()
      .addColumn(COLUMN_CODE, Boolean.TRUE)
      .addAttr(ATTR_CODE, JPType.STRING)
      .addAttr(ATTR_CODE_COPY, JPType.STRING)
      .addColumn(COLUMN_NAME, Boolean.FALSE)
      .addAttr(ATTR_NAME, JPType.STRING)
      .build();

  JPMapRules<String> RULES_BASIC = JPMapRulesNamedBean.newBuilder()
      .addColumn(COLUMN_CODE, Boolean.TRUE)
      .addAttr(ATTR_CODE, JPType.STRING)
      .addColumn(COLUMN_NAME, Boolean.FALSE)
      .addAttr(ATTR_NAME, JPType.STRING)
      .build();

  JPMapRules<String> RULES_WRONG_MAP = JPMapRulesNamedBean.newBuilder()
      .addColumn("sdfdgdf", Boolean.TRUE)
      .addAttr(ATTR_CODE, JPType.STRING)
      .addColumn("ssdff", Boolean.FALSE)
      .addAttr(ATTR_NAME, JPType.STRING)
      .build();

  JPMapRules<Integer> RULES_NUMBER_MAP = JPMapRulesIndexedBean.newBuilder()
      .addColumn(1, Boolean.TRUE)
      .addAttr(ATTR_CODE, JPType.STRING)
      .addColumn(2, Boolean.FALSE)
      .addAttr(ATTR_NAME, JPType.STRING)
      .build();

  JPMapRules<Integer> RULES_NUMBER_MAP_OUT_OF_BOUNDS = JPMapRulesIndexedBean.newBuilder()
      .addColumn(1, Boolean.TRUE)
      .addAttr(ATTR_CODE, JPType.STRING)
      .addColumn(5, Boolean.FALSE)
      .addAttr(ATTR_NAME, JPType.STRING)
      .build();

  JPMapRules<String> RULES_WITH_DATE = JPMapRulesNamedBean.newBuilder()
      .addColumn(COLUMN_DATE, Boolean.TRUE)
      .addAttr(ATTR_DATE, JPType.DATE)
      .addColumn(COLUMN_NAME, Boolean.FALSE)
      .addAttr(ATTR_NAME, JPType.STRING)
      .build();

  private final static JPCsvReaderSettingsBean PARSER_SETTINGS_WITH_HEADERS = JPCsvReaderSettingsBean.newBuilder()
      .encoding(StandardCharsets.UTF_8.name())
      .ignoreQuite(Boolean.FALSE)
      .hasHeaders(Boolean.TRUE)
      .delimiter(';')
      .build();

  private final static JPCsvReaderSettingsBean PARSER_SETTINGS_MAP_MANY = JPCsvReaderSettingsBean.newBuilder()
      .encoding(StandardCharsets.UTF_8.name())
      .ignoreQuite(Boolean.FALSE)
      .hasHeaders(Boolean.TRUE)
      .delimiter(';')
      .build();

  private final static JPCsvReaderSettingsBean PARSER_SETTINGS_NOT_PARSABLE_VALUES = JPCsvReaderSettingsBean.newBuilder()
      .encoding(StandardCharsets.UTF_8.name())
      .ignoreQuite(Boolean.FALSE)
      .hasHeaders(Boolean.TRUE)
      .delimiter(';')
      .build();

  private final static JPCsvReaderSettingsBean PARSER_SETTINGS_WITH_HEADERS_IGNORE_QUOTE = JPCsvReaderSettingsBean.newBuilder()
      .encoding(StandardCharsets.UTF_8.name())
      .ignoreQuite(Boolean.TRUE)
      .quote('\'')
      .hasHeaders(Boolean.TRUE)
      .delimiter(';')
      .build();

  private final static JPCsvReaderSettingsBean PARSER_SETTINGS_NO_HEADERS = JPCsvReaderSettingsBean.newBuilder()
      .encoding(StandardCharsets.UTF_8.name())
      .ignoreQuite(Boolean.FALSE)
      .hasHeaders(Boolean.FALSE)
      .delimiter(';')
      .build();

  private final static JPCsvReaderSettingsBean PARSER_SETTINGS_WITH_HEADERS_SKIPLINES = JPCsvReaderSettingsBean.newBuilder()
      .encoding(StandardCharsets.UTF_8.name())
      .ignoreQuite(Boolean.FALSE)
      .hasHeaders(Boolean.TRUE)
      .skipLines(3)
      .delimiter(';')
      .build();

  private final static JPCsvReaderSettingsBean PARSER_SETTINGS_WRONG_RULES = JPCsvReaderSettingsBean.newBuilder()
      .encoding(StandardCharsets.UTF_8.name())
      .ignoreQuite(Boolean.TRUE)
      .quote('\'')
      .hasHeaders(Boolean.TRUE)
      .delimiter(';')
      .build();

  @PostConstruct
  void beforeAll() {
    Mockito.when(mockParserService.parseTo(String.class, "620-001")).thenReturn("620-001");
    Mockito.when(mockParserService.parseTo(String.class, "ТЕРРИТОРИАЛЬНЫЙ ПУНКТ В Р.П.ЕРМИШЬ МЕЖРАЙОННОГО ОТДЕЛЕНИЯ УФМС РОССИИ ПО РЯЗАНСКОЙ ОБЛАСТИ В Г.САСОВО"))
        .thenReturn("ТЕРРИТОРИАЛЬНЫЙ ПУНКТ В Р.П.ЕРМИШЬ МЕЖРАЙОННОГО ОТДЕЛЕНИЯ УФМС РОССИИ ПО РЯЗАНСКОЙ ОБЛАСТИ В Г.САСОВО");

    jpCsvParserService.setParserService(mockParserService);
  }

  /**
   * Проверка, когда в файле есть заголовки, нет кавычек, правила заданы верно
   */
  @Test
  void testCsvParserWithHeadersWithoutQuote() {
    int expected = 1;

    try (InputStream is = withHeadersWithoutQuote.getInputStream()) {
      Collection<JPMap> parseResult = jpCsvParserService.parse(is, PARSER_SETTINGS_WITH_HEADERS, RULES_BASIC);
      assertThat(parseResult.size()).isEqualTo(expected);
      parseResult.forEach(x -> {
        String code = x.get(ATTR_CODE);
        String name = x.get(ATTR_NAME);
        assertThat(code).isEqualTo(CODE);
        assertThat(name).isEqualTo(NAME);
      });
    } catch (IOException e) {
      fail("Не получилось открыть поток данных");
    }
  }

  /**
   * Проверка, когда в файле есть заголовки, есть кавычки, обязательное поле оказалось пустым между кавычками
   */
  @Test
  void testCsvParserEmptyMandatoryFields() {
    int expected = 0;

    try (InputStream is = withHeadersWithQuoteWithEmptyMandatoryValues.getInputStream()) {
      Collection<JPMap> parseResult = jpCsvParserService.parse(is, PARSER_SETTINGS_WITH_HEADERS_IGNORE_QUOTE, RULES_BASIC);
      assertThat(parseResult.size()).isEqualTo(expected);
    } catch (IOException e) {
      fail("Не получилось открыть поток данных");
    }
  }

  /**
   * Проверка, когда в файле есть заголовки, есть кавычки, обязательное поле оказалось пустым между разделителем
   */
  @Test
  void testCsvParserNoMandatoryFields() {
    int expected = 0;

    try (InputStream is = withHeadersWithQuoteWithNoMandatoryValues.getInputStream()) {
      Collection<JPMap> parseResult = jpCsvParserService.parse(is, PARSER_SETTINGS_WITH_HEADERS_IGNORE_QUOTE, RULES_BASIC);
      assertThat(parseResult.size()).isEqualTo(expected);
    } catch (IOException e) {
      fail("Не получилось открыть поток данных");
    }
  }

  /**
   * Проверка, когда в файле есть заголовки, есть кавычки, обязательное поле состоит из пробелов между разделителем
   */
  @Test
  void testCsvParserBlankMandatoryFields() {
    int expected = 0;

    try (InputStream is = withHeadersWithQuoteWithBlankMandatoryValues.getInputStream()) {
      Collection<JPMap> parseResult = jpCsvParserService.parse(is, PARSER_SETTINGS_WITH_HEADERS_IGNORE_QUOTE, RULES_BASIC);
      assertThat(parseResult.size()).isEqualTo(expected);
    } catch (IOException e) {
      fail("Не получилось открыть поток данных");
    }
  }

  /**
   * Проверка, когда в файле есть заголовки, есть кавычки, правила заданы верно
   */
  @Test
  void testCsvParserIgnoreQuote() {
    int expected = 1;

    try (InputStream is = withHeadersWithoutQuote.getInputStream()) {
      Collection<JPMap> parseResult = jpCsvParserService.parse(is, PARSER_SETTINGS_WITH_HEADERS_IGNORE_QUOTE, RULES_BASIC);
      assertThat(parseResult.size()).isEqualTo(expected);
      parseResult.forEach(x -> {
        String code = x.get(ATTR_CODE);
        String name = x.get(ATTR_NAME);
        assertThat(code).isEqualTo(CODE);
        assertThat(name).isEqualTo(NAME);
      });

    } catch (IOException e) {
      fail("Не получилось открыть поток данных");
    }
  }

  /**
   * Проверка, когда неверно заданы ключи маппинга с заголовками
   */
  @Test
  void testCsvParserWrongRules() {
    try (InputStream is = withHeadersWithoutQuote.getInputStream()) {
      assertThatThrownBy(() -> jpCsvParserService.parse(is, PARSER_SETTINGS_WRONG_RULES, RULES_WRONG_MAP))
          .isInstanceOf(JPRuntimeException.class)
          .hasMessageContaining("Формат файла не соответствует заданным правилам");
    } catch (IOException e) {
      fail("Не получилось открыть поток данных");
    }
  }

  /**
   * Проверка, когда в файле нет заголовков, нет кавычек, правила заданы верно
   */
  @Test
  void testCsvParserNoHeadersNoQuote() {
    int expected = 1;

    Mockito.when(mockParserService.parseTo(Integer.class, 1)).thenReturn(1);
    Mockito.when(mockParserService.parseTo(Integer.class, 2)).thenReturn(2);

    try (InputStream is = noHeadersWithoutQuote.getInputStream()) {
      Collection<JPMap> parseResult = jpCsvParserService.parse(is, PARSER_SETTINGS_NO_HEADERS, RULES_NUMBER_MAP);
      assertThat(parseResult.size()).isEqualTo(expected);
      parseResult.forEach(x -> {
        String code = x.get(ATTR_CODE);
        String name = x.get(ATTR_NAME);
        assertThat(code).isEqualTo(CODE);
        assertThat(name).isEqualTo(NAME);
      });
    } catch (IOException e) {
      fail("Не получилось открыть поток данных");
    }
  }

  /**
   * Проверка, когда в файле только заголовки
   */
  @Test
  void testCsvParserOnlyHeader() {
    int expected = 0;

    try (InputStream is = onlyHeaders.getInputStream()) {
      Collection<JPMap> parseResult = jpCsvParserService.parse(is, PARSER_SETTINGS_WITH_HEADERS_IGNORE_QUOTE, RULES_BASIC);
      assertThat(parseResult.size()).isEqualTo(expected);
    } catch (IOException e) {
      fail("Не получилось открыть поток данных");
    }
  }

  /**
   * Проверка, когда содержатся строки, которые невозможно привести к заданному типу
   */
  @Test
  void testCsvParserNotParsableValues() {
    int expected = 1;

    Mockito.when(mockParserService.parseTo(LocalDate.class, "djjdjdjdjdjd")).thenReturn(null);
    Mockito.when(mockParserService.parseTo(LocalDate.class, "2023-11-11")).thenReturn(LocalDate.of(2023, 11, 11));

    try (InputStream is = notParsableValues.getInputStream()) {
      Collection<JPMap> parseResult = jpCsvParserService.parse(is, PARSER_SETTINGS_NOT_PARSABLE_VALUES, RULES_WITH_DATE);
      assertThat(parseResult.size()).isEqualTo(expected);
      parseResult.forEach(x -> {
        LocalDate date = x.get(ATTR_DATE);
        String name = x.get(ATTR_NAME);
        assertThat(date).isEqualTo(DATE);
        assertThat(name).isEqualTo(NAME);
      });
    } catch (IOException e) {
      fail("Не получилось открыть поток данных");
    }
  }

  /**
   * Проверка, когда в ходе парсинга типа бросается Exception
   */
  @Test
  void testCsvParserNotParsableException() {
    int expected = 1;
    Mockito.when(mockParserService.parseTo(LocalDate.class, "djjdjdjdjdjd")).thenThrow(JPRuntimeException.class);
    Mockito.when(mockParserService.parseTo(LocalDate.class, "2023-11-11")).thenReturn(DATE);

    try (InputStream is = notParsableValues.getInputStream()) {
      Collection<JPMap> parseResult = jpCsvParserService.parse(is, PARSER_SETTINGS_NOT_PARSABLE_VALUES, RULES_WITH_DATE);
      assertThat(parseResult.size()).isEqualTo(expected);
      parseResult.forEach(x -> {
        LocalDate date = x.get(ATTR_DATE);
        String name = x.get(ATTR_NAME);
        assertThat(date).isEqualTo(DATE);
        assertThat(name).isEqualTo(NAME);
      });
    } catch (IOException e) {
      fail("Не получилось открыть поток данных");
    }
  }

  /**
   * Проверка на пустой файл
   */
  @Test
  void testCsvParserEmptyFile() {
    try (InputStream is = new ByteArrayInputStream("".getBytes())) {
      assertThatThrownBy(() -> jpCsvParserService.parse(is, PARSER_SETTINGS_WITH_HEADERS_SKIPLINES, RULES_BASIC))
          .isInstanceOf(JPAppRuntimeException.class)
          .hasMessageContaining("Пустой файл");
    } catch (IOException e) {
      fail("Не получилось открыть поток данных");
    }
  }

  /**
   * Проверка на отсутствие потока данных
   */
  @Test
  void testCsvParserNullInputStream() {
    assertThatThrownBy(() -> jpCsvParserService.parse(null, PARSER_SETTINGS_WITH_HEADERS_SKIPLINES, RULES_BASIC))
        .isInstanceOf(JPRuntimeException.class)
        .hasMessageContaining("Поток данных пуст");
  }

  /**
   * Проверка, когда правила заданы неправильно: не задан символ кавычек
   */
  @Test
  void testCsvParserIgnoreQuoteQuoteNotSet() {
    assertThatThrownBy(() -> JPCsvReaderSettingsBean.newBuilder()
        .encoding(StandardCharsets.UTF_8.name())
        .ignoreQuite(Boolean.TRUE)
        .hasHeaders(Boolean.TRUE)
        .delimiter(';')
        .build()
    )
        .isInstanceOf(JPRuntimeException.class)
        .hasMessageContaining("Не задан символ кавычек");
  }

  /**
   * Проверка, когда необходимо замапить стоFлбец на множество атрибутов
   */
  @Test
  void testCsvParserMapMany() {
    int expected = 1;

    try (InputStream is = withHeadersWithoutQuote.getInputStream()) {
      Collection<JPMap> parseResult = jpCsvParserService.parse(is, PARSER_SETTINGS_MAP_MANY, RULES_MANY_MAP);
      assertThat(parseResult.size()).isEqualTo(expected);
      parseResult.forEach(x -> {
        String code = x.get(ATTR_CODE);
        String codeCopy = x.get(ATTR_CODE_COPY);
        String name = x.get(ATTR_NAME);
        assertThat(code).isEqualTo(CODE);
        assertThat(codeCopy).isEqualTo(CODE);
        assertThat(name).isEqualTo(NAME);
      });
    } catch (IOException e) {
      fail("Не получилось открыть поток данных");
    }
  }

  /**
   * Проверка, когда необходимо пропустить строк больше, чем количество строк в файле
   */
  @Test
  void testCsvParserSkipLines() {
    int expected = 0;

    try (InputStream is = withHeadersWithoutQuote.getInputStream()) {
      Collection<JPMap> parseResult = jpCsvParserService.parse(is, PARSER_SETTINGS_WITH_HEADERS_SKIPLINES, RULES_BASIC);
      assertThat(parseResult.size()).isEqualTo(expected);
    } catch (IOException e) {
      fail("Не получилось открыть поток данных");
    }
  }

  /**
   * Проверка, когда в правилах не задано имя колонки
   */
  @Test
  void testWrongRulesColumn() {
    assertThatThrownBy(() -> JPMapRulesNamedBean.newBuilder()
        .addColumn("", Boolean.TRUE)
        .addAttr(ATTR_CODE, JPType.STRING)
        .addColumn("NAME", Boolean.FALSE)
        .addAttr(ATTR_NAME, JPType.STRING)
        .build()
    )
        .isInstanceOf(JPRuntimeException.class)
        .hasMessageContaining("Не задано имя колонки");
  }

  /**
   * Проверка, когда в правилах не задано имя атрибута
   */
  @Test
  void testWrongNamedRulesAttr() {
    assertThatThrownBy(() -> JPMapRulesNamedBean.newBuilder()
        .addColumn("CODE", Boolean.TRUE)
        .addAttr("", JPType.STRING)
        .addColumn("NAME", Boolean.FALSE)
        .addAttr(ATTR_NAME, JPType.STRING)
        .build()
    )
        .isInstanceOf(JPRuntimeException.class)
        .hasMessageContaining("Не задано имя атрибута");
  }

  /**
   * Проверка, когда в правилах задан недопустимый индекс столбца
   */
  @Test
  void testWrongRulesAttr() {
    assertThatThrownBy(() -> JPMapRulesIndexedBean.newBuilder()
        .addColumn(1, Boolean.TRUE)
        .addAttr(ATTR_CODE, JPType.STRING)
        .addColumn(-6, Boolean.FALSE)
        .addAttr(ATTR_NAME, JPType.STRING)
        .build()
    )
        .isInstanceOf(JPRuntimeException.class)
        .hasMessageContaining("Индекс столбца не может быть отрицательным");
  }

  /**
   * Проверка, когда правила пустые
   */
  @Test
  void testEmptyRules() {
    assertThatThrownBy(() -> JPMapRulesNamedBean.newBuilder().build()
    )
        .isInstanceOf(JPRuntimeException.class)
        .hasMessageContaining("Не заданы правила");
  }

  /**
   * Проверка, когда индекс колонки выходит за пределы числа столбцов в файле
   */
  @Test
  void testCsvParserIndexOutOfColumnsNumber() {
    try (InputStream is = withHeadersWithoutQuote.getInputStream()) {
      assertThatThrownBy(() -> jpCsvParserService.parse(is, PARSER_SETTINGS_WITH_HEADERS, RULES_NUMBER_MAP_OUT_OF_BOUNDS))
          .isInstanceOf(JPRuntimeException.class)
          .hasMessageContaining("Формат файла не соответствует заданным правилам");
    } catch (IOException e) {
      fail("Не получилось открыть поток данных");
    }
  }
}
