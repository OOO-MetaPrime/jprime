package mp.jprime.requesthistory.beans;

import mp.jprime.requesthistory.RequestHistoryObject;

public final class RequestHistoryObjectBean implements RequestHistoryObject {
  /**
   * Идентификатор
   */
  private final Object id;

  /**
   * Код класса
   */
  private final String classCode;

  /**
   * Тело истории
   */
  private final Object body;

  private RequestHistoryObjectBean(Object id, String classCode, Object body) {
    this.id = id;
    this.classCode = classCode;
    this.body = body;
  }

  public static RequestHistoryObjectBean of(Object id, String classCode, Object body) {
    return new RequestHistoryObjectBean(id, classCode, body);
  }

  @Override
  public Object getId() {
    return id;
  }

  @Override
  public String getClassCode() {
    return classCode;
  }

  @Override
  public Object getBody() {
    return body;
  }
}
