package mp.jprime.imex.csvwriter;

import mp.jprime.dataaccess.beans.JPObject;

import java.io.InputStream;
import java.util.Collection;

/**
 * Сервис выгрузки {@link JPObject} в CSV
 */
public interface JPObjectCsvWriterService {
  /**
   * Запускает выгрузку объектов метаописания класса в архивированный входной поток
   *
   * @param jpClass   Метаописание класса
   * @param attrs     Атрибуты объектов для записи
   * @param filesName Имя файлов в архиве
   * @param fileSize  Количество объектов в файле
   * @return {@link InputStream Входной поток}
   */
  InputStream zipOf(String jpClass, Collection<String> attrs, String filesName, Integer fileSize);

  /**
   * Запускает выгрузку объектов метаописания класса во входной поток
   *
   * @param jpClass   Метаописание класса
   * @param attrs     Атрибуты объектов для записи
   * @param filesName Имя файлов в архиве
   * @return {@link InputStream Входной поток}
   */
  InputStream of(String jpClass, Collection<String> attrs, String filesName);
}
