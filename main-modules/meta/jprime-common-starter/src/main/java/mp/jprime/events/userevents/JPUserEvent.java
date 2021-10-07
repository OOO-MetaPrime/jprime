package mp.jprime.events.userevents;

import mp.jprime.events.userevents.annotations.JPUserEventType;

import java.time.LocalDateTime;

/**
 * Пользовательское событие
 */
public interface JPUserEvent<T> {
  /**
   * Признак "призрачного события". Оно не попадает в хранилище и не отображается в глобальном меню "событий"
   *
   * @return Да/Нет
   */
  boolean isGhost();

  /**
   * Гуид события
   *
   * @return Гуид события
   */
  String getGuid();

  /**
   * Дата события
   *
   * @return Дата события
   */
  LocalDateTime getDate();

  /**
   * Код типа события
   *
   * @return Код типа события
   */
  String getTypeCode();

  /**
   * Заголовок типа события
   *
   * @return Заголовок типа события
   */
  String getTypeTitle();

  /**
   * Описание события
   *
   * @return Описание события
   */
  String getDescription();

  /**
   * Кодовое имя класса объекта, связанного с событием
   *
   * @return Кодовое имя класса объекта, связанного с событием
   */
  String getObjectClassCode();

  /**
   * Идентификатор объекта, связанного с событием
   *
   * @return Идентификатор объекта, связанного с событием
   */
  String getObjectId();

  /**
   * Id пользователя
   *
   * @return Id пользователя
   */
  String getUserId();

  /**
   * Имя пользователя
   *
   * @return Имя пользователя
   */
  String getUserDescription();

  /**
   * Тип события
   *
   * @return Тип события
   */
  default String getEventType() {
    JPUserEventType a = this.getClass().getAnnotation(JPUserEventType.class);
    return a != null ? a.code() : null;
  }

  /**
   * Данные события
   *
   * @return Данные события
   */
  T getEventData();
}
