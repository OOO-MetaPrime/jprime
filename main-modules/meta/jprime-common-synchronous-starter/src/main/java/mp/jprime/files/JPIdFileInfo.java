package mp.jprime.files;

import mp.jprime.dataaccess.beans.JPId;

/**
 * Информация о файле, связанном с объектом мета-класса
 */
public interface JPIdFileInfo extends JPFileInfo {
  /**
   * Идентификатор объекта, к которому относится файл
   */
  JPId getJPId();
}
