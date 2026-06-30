package mp.jprime.parsers.doctypeformat;

/**
 * Приведение строки в соответствие с типом документа
 */
public interface JpDocTypeFormatParser {
  /**
   * Результат разбора данных по виду документа
   */
  interface Result {
    /**
     * Положительный результат обработки
     *
     * @return Да/Нет
     */
    boolean isCheck();

    /**
     * Строка, соответствующая серии
     *
     * @return Серия
     */
    String getParseSeries();

    /**
     * Положительный результат обработки серии
     *
     * @return Да/Нет
     */
    boolean isSeriesCheck();

    /**
     * Строка, соответствующая номеру
     *
     * @return Номер
     */
    String getParseNumber();

    /**
     * Положительный результат обработки номера
     *
     * @return Да/Нет
     */
    boolean isNumberCheck();
  }

  /**
   * Проверяем серию и номер документа "Свидетельство о рождении"
   *
   * @param series серия документа
   * @param number номер документа
   * @return Результат проверки
   */
  Result parseRussiaBirthCertificate(String series, String number);


  /**
   * Проверяем серию и номер документа "Военный билет"
   *
   * @param series серия документа
   * @param number номер документа
   * @return Результат проверки
   */
  Result parseRussiaMilitatyId(String series, String number);

  /**
   * Проверяем серию и номер документа "Паспорт гражданина Российской Федерации"
   *
   * @param series серия документа
   * @param number номер документа
   * @return Результат проверки
   */
  Result parseRussiaPassport(String series, String number);
}
