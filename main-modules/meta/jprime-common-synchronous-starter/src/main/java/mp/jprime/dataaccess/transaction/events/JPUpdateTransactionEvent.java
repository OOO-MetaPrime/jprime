package mp.jprime.dataaccess.transaction.events;

import mp.jprime.dataaccess.Event;
import mp.jprime.dataaccess.params.JPUpdate;
import mp.jprime.security.ConnectionInfo;

/**
 * Событие транзакции - обновление объекта
 */
public final class JPUpdateTransactionEvent implements JPTransactionJPObjectEvent {
  public static final String CODE = "jpUpdateEvent";

  private final Comparable id;
  private final String jpClassCode;
  private final JPUpdate query;
  private final ConnectionInfo connInfo;

  private JPUpdateTransactionEvent(Comparable id, String jpClassCode, JPUpdate query, ConnectionInfo connInfo) {
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
    return Event.UPDATE_SUCCESS;
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

  public JPUpdate getQuery() {
    return query;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder {
    private Comparable id;
    private String jpClassCode;
    private JPUpdate query;
    private ConnectionInfo connInfo;

    private Builder() {
    }

    public JPUpdateTransactionEvent build() {
      return new JPUpdateTransactionEvent(id, jpClassCode, query, connInfo);
    }

    public Builder id(Comparable id) {
      this.id = id;
      return this;
    }

    public Builder jpClassCode(String jpClassCode) {
      this.jpClassCode = jpClassCode;
      return this;
    }

    public Builder query(JPUpdate query) {
      this.query = query;
      return this;
    }

    public Builder connInfo(ConnectionInfo connInfo) {
      this.connInfo = connInfo;
      return this;
    }
  }
}
