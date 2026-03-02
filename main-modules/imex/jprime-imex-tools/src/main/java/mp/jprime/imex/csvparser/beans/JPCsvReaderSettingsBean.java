package mp.jprime.imex.csvparser.beans;

import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.imex.csvparser.JPCsvReaderSettings;
import org.apache.commons.lang3.StringUtils;

/**
 * Настройки CSV парсера
 */
public final class JPCsvReaderSettingsBean implements JPCsvReaderSettings {
  /**
   * Кодировка файла
   */
  private final String encoding;
  /**
   * Признак наличия заголовка
   */
  private final boolean hasHeaders;
  /**
   * Разделитель
   */
  private final char delimiter;
  /**
   * Символ кавычек
   */
  private final Character quote;
  /**
   * Признак игнорирования кавычек
   */
  private final boolean ignoreQuote;
  /**
   * Число строк, пропускаемых парсером
   */
  private final int skipLines;

  private JPCsvReaderSettingsBean(String encoding, boolean hasHeaders, char delimiter, Character quote, boolean ignoreQuote, int skipLines) {
    if (StringUtils.isBlank(encoding)) {
      throw new JPRuntimeException("jp.imex.settingsbean.inputparams.empty", "Не заданы обязательные настройки");
    }

    if (skipLines < 0) {
      throw new JPRuntimeException("jp.imex.settingsbean.skipLines.invalid", "Недопустимое значение пропускаемых строк");
    }

    if (ignoreQuote && quote == null) {
      throw new JPRuntimeException("jp.imex.settingsbean.quote.empty", "Не задан символ кавычек");
    }

    this.encoding = encoding;
    this.hasHeaders = hasHeaders;
    this.delimiter = delimiter;
    this.quote = quote;
    this.ignoreQuote = ignoreQuote;
    this.skipLines = skipLines;
  }

  /**
   * Кодировка файла
   *
   * @return Кодировка
   */
  @Override
  public String getEncoding() {
    return encoding;
  }

  /**
   * Признак наличия заголовка
   *
   * @return Признак наличия заголовка
   */
  @Override
  public boolean hasHeaders() {
    return hasHeaders;
  }

  /**
   * Разделитель столбцов
   *
   * @return Разделитель столбцов
   */
  @Override
  public char getDelimiter() {
    return delimiter;
  }

  /**
   * Символ кавычек
   *
   * @return Символ кавычек
   */
  @Override
  public Character getQuote() {
    return quote;
  }

  /**
   * Признак игнорирование кавычек
   *
   * @return Признак игнорирование кавычек
   */
  @Override
  public boolean isIgnoreQuote() {
    return ignoreQuote;
  }

  /**
   * Число строк, пропускаемых парсером
   *
   * @return Число строк, пропускаемых парсером
   */
  @Override
  public int getSkipLines() {
    return skipLines;
  }

  /**
   * Создаёт построитель объекта {@link JPCsvReaderSettingsBean}
   *
   * @return Построитель объекта {@link JPCsvReaderSettingsBean}
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель {@link JPCsvReaderSettingsBean}
   */
  public static final class Builder {
    private String encoding;
    private boolean hasHeaders;
    private char delimiter;
    private Character quote;
    private boolean ignoreQuote;
    private int skipLines;

    private Builder() {
    }

    public Builder encoding(String encoding) {
      this.encoding = encoding;
      return this;
    }

    public Builder hasHeaders(boolean hasHeaders) {
      this.hasHeaders = hasHeaders;
      return this;
    }

    public Builder delimiter(char delimiter) {
      this.delimiter = delimiter;
      return this;
    }

    public Builder quote(Character quote) {
      this.quote = quote;
      return this;
    }

    public Builder skipLines(int skipLines) {
      this.skipLines = skipLines;
      return this;
    }

    public Builder ignoreQuite(boolean ignoreQuote) {
      this.ignoreQuote = ignoreQuote;
      return this;
    }

    /**
     * Создает новые настройки для CSV парсера
     *
     * @return Настройки для CSV парсера {@link JPCsvReaderSettingsBean}
     */
    public JPCsvReaderSettingsBean build() {
      return new JPCsvReaderSettingsBean(encoding, hasHeaders, delimiter, quote, ignoreQuote, skipLines);
    }
  }
}
