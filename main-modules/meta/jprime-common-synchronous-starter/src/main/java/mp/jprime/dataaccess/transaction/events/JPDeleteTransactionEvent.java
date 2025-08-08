package mp.jprime.dataaccess.transaction.events;

import mp.jprime.dataaccess.Event;
import mp.jprime.dataaccess.params.JPDelete;
import mp.jprime.security.ConnectionInfo;

/**
 * Событие транзакции - удаление объекта
 */
public final class JPDeleteTransactionEvent implements JPTransactionJPObjectEvent {
  public static final String CODE = "jpDeleteEvent";

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

  @Override
  public String getCode() {
    return CODE;
  }

  @Override
  public Event getEvent() {
    return Event.DELETE_SUCCESS;
  }

  @Override
  public Comparable getId() {
    return id;
  }

  @Override
  public String getJpClassCode() {
    return jpClassCode;
  }

  @Override
  public ConnectionInfo getConnInfo() {
    return connInfo;
  }

  public JPDelete getQuery() {
    return query;
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
