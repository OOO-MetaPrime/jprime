package mp.jprime.utils;

import java.util.Collection;
import java.util.Collections;

/**
 * Входящие параметры утилиты
 */
public interface JPUtilInParams<R> {
  /**
   * кодовое имя метакласса корневого объекта
   *
   * @return кодовое имя метакласса корневого объекта
   */
  String getRootObjectClassCode();

  /**
   * идентификатор корневого объекта
   *
   * @return идентификатор корневого объекта
   */
  R getRootObjectId();

  /**
   * кодовое имя метакласса объекта/ов
   *
   * @return кодовое имя метакласса объекта/ов
   */
  String getObjectClassCode();

  /**
   * идентификатор или идентификаторы объектов указанного класса
   *
   * @return идентификатор или идентификаторы объектов указанного класса
   */
  Collection<R> getObjectIds();

  /**
   * Возвращает информацию о корневом объекте. rootObjectId, если указан, иначе - objectIds
   *
   * @return Информация о корневом объекте
   */
  default RootInfo<R> getRootInfo() {
    String classCode;
    Collection<R> ids;
    if (this.getRootObjectClassCode() != null) {
      classCode = this.getRootObjectClassCode();
      ids = Collections.singleton(this.getRootObjectId());
    } else {
      classCode = this.getObjectClassCode();
      ids = this.getObjectIds();
    }
    return new RootInfo<>(classCode, ids);
  }

  /**
   * Информация о корневом объекте
   *
   * @param <R> Тип идентификатора
   */
  class RootInfo<R> {
    private final String classCode;
    private final Collection<R> ids;

    private RootInfo(String classCode, Collection<R> ids) {
      this.classCode = classCode;
      this.ids = Collections.unmodifiableCollection(ids != null ? ids : Collections.emptyList());
    }

    public String getClassCode() {
      return classCode;
    }

    public Collection<R> getIds() {
      return ids;
    }
  }
}
