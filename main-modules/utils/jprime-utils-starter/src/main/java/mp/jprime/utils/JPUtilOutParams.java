package mp.jprime.utils;

import mp.jprime.utils.annotations.JPUtilResultType;

/**
 * Исходящие параметры утилиты
 */
public interface JPUtilOutParams<T> {
  /**
   * Описание результата
   *
   * @return Описание результата
   */
  String getDescription();

  /**
   * Полное кодовое имя описаний результата
   *
   * @return Полное кодовое имя описаний результата
   */
  String getQName();

  /**
   * Тип результата
   *
   * @return Тип результата
   */
  default String getResultType() {
    JPUtilResultType a = this.getClass().getAnnotation(JPUtilResultType.class);
    return a != null ? a.code() : null;
  }

  /**
   * Признак изменения исходных данных
   *
   * @return Да/Нет
   */
  boolean isChangeData();

  /**
   * Результат
   *
   * @return Результат
   */
  T getResult();
}
