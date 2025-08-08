package mp.jprime.dataaccess.transaction.events;

import mp.jprime.dataaccess.Event;
import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.security.ConnectionInfo;

/**
 * Событие транзакции - создание объекта
 */
public final class JPCreateTransactionEvent implements JPTransactionJPObjectEvent {
  public static final String CODE = "jpCreateEvent";

  private final Comparable id;
  private final String jpClassCode;
  private final JPCreate query;
  private final ConnectionInfo connInfo;

  private JPCreateTransactionEvent(Comparable id, String jpClassCode, JPCreate query, ConnectionInfo connInfo) {
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
    return Event.CREATE_SUCCESS;
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

  public JPCreate getQuery() {
    return query;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder {
    private Comparable id;
    private String jpClassCode;
    private JPCreate query;
    private ConnectionInfo connInfo;

    private Builder() {
    }

    public JPCreateTransactionEvent build() {
      return new JPCreateTransactionEvent(id, jpClassCode, query, connInfo);
    }

    public Builder id(Comparable id) {
      this.id = id;
      return this;
    }

    public Builder jpClassCode(String jpClassCode) {
      this.jpClassCode = jpClassCode;
      return this;
    }

    public Builder query(JPCreate query) {
      this.query = query;
      return this;
    }

    public Builder connInfo(ConnectionInfo connInfo) {
      this.connInfo = connInfo;
      return this;
    }
  }
}
