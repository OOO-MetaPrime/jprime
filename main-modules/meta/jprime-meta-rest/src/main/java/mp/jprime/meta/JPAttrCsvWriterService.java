package mp.jprime.meta;

import java.io.InputStream;

/**
 * Сервис выгрузки {@link JPAttr} в CSV
 */
public interface JPAttrCsvWriterService {
  /**
   * Запускает выгрузку атрибутов метаописания класса во входной поток
   *
   * @param jpClass Метаописание класса
   * @param lineEnd Окончание строки
   * @return {@link InputStream Входной поток}
   */
  InputStream of(JPClass jpClass, String lineEnd);

  /**
   * Запускает выгрузку атрибутов метаописания класса во входной поток
   *
   * @param jpClass Метаописание класса
   * @return {@link InputStream Входной поток}
   */
  InputStream of(JPClass jpClass);
}
