package mp.jprime.imex.csvparser;

import java.nio.charset.Charset;

/**
 * Настройки парсера
 */
public interface JPCsvReaderSettings {
  /**
   * 8-битная стандартная кодировка русских версий Microsoft Windows,
   * также известная как CP1251
   */
  Charset WINDOWS_1251 = Charset.forName("windows-1251");

  /**
   * Кодировка файла
   *
   * @return Кодировка
   */
  String getEncoding();

  /**
   * Признак наличия заголовка
   *
   * @return Признак наличия заголовка
   */
  boolean hasHeaders();

  /**
   * Разделитель столбцов
   *
   * @return Разделитель столбцов
   */
  char getDelimiter();

  /**
   * Символ кавычек
   *
   * @return Символ кавычек
   */
  Character getQuote();

  /**
   * Признак игнорирование кавычек
   *
   * @return Признак игнорирование кавычек
   */
  boolean isIgnoreQuote();

  /**
   * Число строк, пропускаемых парсером
   *
   * @return Число строк, пропускаемых парсером
   */
  int getSkipLines();
}
