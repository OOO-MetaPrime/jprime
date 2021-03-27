package mp.jprime.dataaccess.transaction.events;

import mp.jprime.dataaccess.Event;
import mp.jprime.dataaccess.params.JPDelete;
import mp.jprime.security.ConnectionInfo;

/**
 * Событие транзакции - удаление объекта
 */
public final class JPDeleteTransactionEvent implements TransactionEvent {
  private final Comparable id;
  private final String jpClassCode;
  private final JPDelete query;
  private final ConnectionInfo connInfo;

  private JPDeleteTransactionEvent(Comparable id, String jpClassCode, JPDelete query, ConnectionInfo connInfo) {
    this.id = id;
    this.jpClassCode = jpClassCode;
    this.query = query;
    this.connInfo = connInfo;
  }

  /**
   * Возвращает событие
   *
   * @return Событие
   */
  @Override
  public Event getEvent() {
    return Event.DELETE_SUCCESS;
  }

  /**
   * Идентификатор объекта
   *
   * @return Идентификатор объекта
   */
  @Override
  public Comparable getId() {
    return id;
  }

  /**
   * Кодовое имя класса
   *
   * @return Кодовое имя класса
   */
  @Override
  public String getJpClassCode() {
    return jpClassCode;
  }

  /**
   * Возвращает запрос
   *
   * @return JPDelete
   */
  public JPDelete getQuery() {
    return query;
  }

  /**
   * Источник события
   *
   * @return Источник события
   */
  @Override
  public ConnectionInfo getConnInfo() {
    return connInfo;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder {
    private Comparable id;
    private String jpClassCode;
    private JPDelete query;
    private ConnectionInfo connInfo;

    private Builder() {
    }

    public JPDeleteTransactionEvent build() {
      return new JPDeleteTransactionEvent(id, jpClassCode, query, connInfo);
    }

    public Builder id(Comparable id) {
      this.id = id;
      return this;
    }

    public Builder jpClassCode(String jpClassCode) {
      this.jpClassCode = jpClassCode;
      return this;
    }

    public Builder query(JPDelete jsonBody) {
      this.query = jsonBody;
      return this;
    }

    public Builder connInfo(ConnectionInfo connInfo) {
      this.connInfo = connInfo;
      return this;
    }
  }
}
