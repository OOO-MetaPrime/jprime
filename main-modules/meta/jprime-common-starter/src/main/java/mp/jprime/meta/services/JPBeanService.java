package mp.jprime.meta.services;

import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.beans.JPLinkedData;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.meta.JPClass;

import java.util.Collection;
import java.util.Map;

/**
 * Логика работы с JPBean
 */
public interface JPBeanService {
  /**
   * Создает объект
   *
   * @param jpClassCode        Кодовое имя класса
   * @param primaryKeyAttrCode Кодовое имя атрибута-идентификатора
   * @param data               Данные объекта
   * @return Новый объект
   */
  JPObject newInstance(String jpClassCode, String primaryKeyAttrCode, Map<String, Object> data);

  /**
   * Создает объект
   *
   * @param jpClass Метаописание класса
   * @param data    Данные объекта
   * @return Новый объект
   */
  JPObject newInstance(JPClass jpClass, Map<String, Object> data);

  /**
   * Создает объект
   *
   * @param jpClass Метаописание класса
   * @param jpData  Данные объекта
   * @return Новый объект
   */
  default JPObject newInstance(JPClass jpClass, JPData jpData) {
    return newInstance(jpClass, jpData, null);
  }

  /**
   * Создает объект
   *
   * @param jpClass      Метаописание класса
   * @param jpData       Данные объекта
   * @param jpLinkedData Данные связанных объектов
   * @return Новый объект
   */
  JPObject newInstance(JPClass jpClass, JPData jpData, JPLinkedData jpLinkedData);

  /**
   * Возвращает список атрибутов  о-умолчанию
   *
   * @param jpClass Кодовое имя метаописания класса
   * @return Список атрибутов по умолчанию
   */
  Collection<String> getDefaultJpAttrs(String jpClass);
}
