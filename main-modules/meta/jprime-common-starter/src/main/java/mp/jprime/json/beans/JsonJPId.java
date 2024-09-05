package mp.jprime.json.beans;

import mp.jprime.dataaccess.beans.JPId;

/*
 * Модель данных ответа получения jpId
 */
public class JsonJPId {
  private final Comparable id;
  private final String classCode;

  /**
   * Конструктор
   *
   * @param id        Идентификатор объекта
   * @param classCode Кодовое имя класса
   */
  private JsonJPId(Comparable id, String classCode) {
    this.id = id;
    this.classCode = classCode;
  }

  public Comparable getId() {
    return id;
  }

  public String getClassCode() {
    return classCode;
  }

  public static JsonJPId of(JPId id) {
    return id != null ? new JsonJPId(id.getId(), id.getJpClass()) : null;
  }

  public static JsonJPId of(Comparable id, String classCode) {
    return id != null ? new JsonJPId(id, classCode) : null;
  }
}
