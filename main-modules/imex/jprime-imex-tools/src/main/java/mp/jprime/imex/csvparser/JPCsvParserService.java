package mp.jprime.imex.csvparser;

import mp.jprime.imex.rules.JPMapRules;
import mp.jprime.lang.JPMap;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

/**
 * Сервис для парсинга файла
 */
public interface JPCsvParserService {
  /**
   * Парсит файл с заданными настройками и возвращает набор строк файла.
   * Если задан заголовок, то пропуск строк происходит уже после него
   *
   * @param is            Поток данных
   * @param parseSettings {@link JPCsvReaderSettings Настройки парсера}
   * @return Коллекция строк
   */
  Collection<List<String>> parse(InputStream is, JPCsvReaderSettings parseSettings);

  /**
   * Парсит файл с заданными настройками и возвращает сопоставленную коллекцию.
   * Если задан заголовок, то пропуск строк происходит уже после него
   *
   * @param is            Поток данных
   * @param parseSettings {@link JPCsvReaderSettings Настройки парсера}
   * @param rules         {@link JPMapRules Правила маппинга}
   * @return Коллекция объектов, сопоставленных по правилам
   */
  <T> Collection<JPMap> parse(InputStream is, JPCsvReaderSettings parseSettings, JPMapRules<T> rules);
}
