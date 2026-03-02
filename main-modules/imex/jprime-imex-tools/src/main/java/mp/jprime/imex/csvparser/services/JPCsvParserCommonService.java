package mp.jprime.imex.csvparser.services;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.exceptions.JPAppRuntimeException;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.imex.csvparser.JPCsvParserService;
import mp.jprime.imex.csvparser.JPCsvReaderSettings;
import mp.jprime.imex.rules.JPMapRules;
import mp.jprime.lang.JPMap;
import mp.jprime.meta.beans.JPType;
import mp.jprime.parsers.ParserService;
import mp.jprime.parsers.ParserServiceAware;
import org.apache.tika.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Базовая реализация сервиса для парсинга файла с использованием библиотеки opencsv
 */
@Service
public final class JPCsvParserCommonService implements JPCsvParserService, ParserServiceAware {
  private ParserService parserService;

  @Override
  public void setParserService(ParserService parserService) {
    this.parserService = parserService;
  }

  /**
   * Парсит файл с заданными настройками и возвращает набор строк файла.
   * Если задан заголовок, то пропуск строк происходит уже после него
   *
   * @param is            Поток данных
   * @param parseSettings {@link JPCsvReaderSettings Настройки парсера}
   * @return Коллекция строк файла
   */
  @Override
  public Collection<List<String>> parse(InputStream is, JPCsvReaderSettings parseSettings) {
    if (is == null) {
      throw new JPRuntimeException("jp.imex.csvparser.inputstream.empty", "Поток данных пуст");
    }

    if (parseSettings == null) {
      throw new JPRuntimeException("jp.imex.csvparser.parseSettings.empty", "Не заданы настройки парсера");
    }

    try {
      CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(is, parseSettings.getEncoding()))
          .withCSVParser(getParser(parseSettings))
          .build();

      if (csvReader.peek() == null) {
        throw new JPAppRuntimeException("jp.imex.csvparser.file.empty", "Пустой файл");
      }

      Collection<List<String>> result = new ArrayList<>();
      if (parseSettings.hasHeaders()) {
        result.add(Arrays.asList(csvReader.readNext()));
      }

      csvReader.skip(parseSettings.getSkipLines());

      String[] line;
      while ((line = csvReader.readNext()) != null) {
        List<String> values = new ArrayList<>(line.length);
        for (String value : line) {
          values.add(StringUtils.isBlank(value) ? null : value);
        }
        result.add(values);
      }

      return result;

    } catch (IOException | CsvException e) {
      throw new JPRuntimeException("Неожиданная ошибка при чтении файла: " + e.getMessage(), e);
    }
  }

  /**
   * Парсит файл с заданными настройками и возвращает сопоставленную коллекцию.
   * Если задан заголовок, то пропуск строк происходит уже после него
   *
   * @param is            Поток данных
   * @param parseSettings {@link JPCsvReaderSettings Настройки парсера}
   * @param rules         {@link JPMapRules Правила маппинга}
   * @return Коллекция объектов, сопоставленных по правилам
   */
  @Override
  public <T> Collection<JPMap> parse(InputStream is, JPCsvReaderSettings parseSettings, JPMapRules<T> rules) {
    if (rules == null) {
      throw new JPRuntimeException("jp.imex.csvparser.rules.empty", "Не заданы правила маппинга");
    }

    Collection<List<String>> rows = parse(is, parseSettings);

    if (rows.isEmpty()) {
      return Collections.emptyList();
    }

    Iterator<List<String>> iterator = rows.iterator();
    Map<Integer, JPMapRules.JPColumnSettings> attrIndex = mapColumns(iterator, rows.iterator().next().size(), rules);

    if (attrIndex.size() != rules.getColumns().size()) {
      throw new JPRuntimeException("jp.imex.csvparser.match.invalid", "Формат файла не соответствует заданным правилам");
    }

    Collection<JPMap> result = new ArrayList<>();
    while (iterator.hasNext()) {
      Map<String, Object> stringObjectMap = getLine(iterator.next(), attrIndex);

      if (stringObjectMap != null) {
        result.add(JPData.of(stringObjectMap));
      }
    }

    return result;
  }

  private CSVParser getParser(JPCsvReaderSettings parseSettings) {
    boolean ignoreQuote = parseSettings.isIgnoreQuote();
    CSVParserBuilder builder = new CSVParserBuilder();

    if (ignoreQuote) {
      builder.withQuoteChar(parseSettings.getQuote());
    }

    return builder
        .withSeparator(parseSettings.getDelimiter())
        .withIgnoreQuotations(ignoreQuote)
        .build();
  }

  private <T> Map<Integer, JPMapRules.JPColumnSettings> mapColumns(Iterator<List<String>> iterator, Integer numOfColumns, JPMapRules<T> rules) {
    Map<Integer, JPMapRules.JPColumnSettings> attrIndex = new HashMap<>();

    // если есть имена столбцов
    if (rules.getKeyType() == JPType.STRING) {
      List<String> headers = iterator.next();

      for (int i = 0; i < headers.size(); i++) {
        JPMapRules<String> stringRules = rules.toStringKeyType();
        JPMapRules.JPColumnSettings settings = stringRules.getSettings(headers.get(i));

        if (settings != null) {
          attrIndex.put(i, settings);
        }
      }
    } else {
      JPMapRules<Integer> intRules = rules.toIntegerKeyType();
      intRules.getColumns().forEach(index -> {
        if (index < numOfColumns) {
          attrIndex.put(index, intRules.getSettings(index));
        }
      });
    }

    return attrIndex;
  }

  private Map<String, Object> getLine(List<String> line, Map<Integer, JPMapRules.JPColumnSettings> attrIndex) {
    boolean correctLine = true;
    Map<String, Object> keyValueMap = new HashMap<>();

    for (Map.Entry<Integer, JPMapRules.JPColumnSettings> entry : attrIndex.entrySet()) {
      Integer columnIndex = entry.getKey();
      JPMapRules.JPColumnSettings columnSetting = entry.getValue();
      String columnValue = line.get(columnIndex);

      for (String mapName : columnSetting.getAttrs()) {
        JPType attributeType = columnSetting.getAttrType(mapName);

        if (columnValue == null || columnValue.isBlank()) {
          if (columnSetting.isMandatory()) {
            correctLine = false;
            break;
          } else {
            keyValueMap.put(mapName, null);
          }
        } else {
          Object parsedColumnValue;

          try {
            parsedColumnValue = parserService.parseTo(attributeType.getJavaClass(), columnValue);
          } catch (Exception e) {
            correctLine = false;
            break;
          }

          if (parsedColumnValue == null) {
            correctLine = false;
            break;
          } else {
            keyValueMap.put(mapName, parsedColumnValue);
          }
        }
      }
    }

    if (correctLine) {
      return keyValueMap;
    } else {
      return null;
    }
  }
}
