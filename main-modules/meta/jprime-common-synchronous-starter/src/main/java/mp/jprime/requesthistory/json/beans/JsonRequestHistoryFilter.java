package mp.jprime.requesthistory.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonRequestHistoryFilter {
  /**
   * Код класса
   */
  private Collection<String> classCode;
  /**
   * Идентификатор пользователя
   */
  private Collection<String> userId;
  /**
   * Логин пользователя
   */
  private Collection<String> username;
  /**
   * Дата запроса с
   */
  private LocalDateTime requestDateFrom;
  /**
   * Дата запроса по
   */
  private LocalDateTime requestDateTo;
  /**
   * Идентификатор объекта
   */
  private Collection<String> objectId;

  public Collection<String> getClassCode() {
    return classCode;
  }

  public void setClassCode(Collection<String> classCode) {
    this.classCode = classCode;
  }

  public Collection<String> getUserId() {
    return userId;
  }

  public void setUserId(Collection<String> userId) {
    this.userId = userId;
  }

  public Collection<String> getUsername() {
    return username;
  }

  public void setUsername(Collection<String> username) {
    this.username = username;
  }

  public LocalDateTime getRequestDateFrom() {
    return requestDateFrom;
  }

  public void setRequestDateFrom(LocalDateTime requestDateFrom) {
    this.requestDateFrom = requestDateFrom;
  }

  public LocalDateTime getRequestDateTo() {
    return requestDateTo;
  }

  public void setRequestDateTo(LocalDateTime requestDateTo) {
    this.requestDateTo = requestDateTo;
  }

  public Collection<String> getObjectId() {
    return objectId;
  }

  public void setObjectId(Collection<String> objectId) {
    this.objectId = objectId;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder {
    private Collection<String> classCode;
    private Collection<String> userId;
    private Collection<String> username;
    private LocalDateTime requestDateFrom;
    private LocalDateTime requestDateTo;
    private Collection<String> objectId;

    private Builder() {
    }

    public Builder classCode(Collection<String> classCode) {
      this.classCode = classCode;
      return this;
    }

    public Builder userId(Collection<String> userId) {
      this.userId = userId;
      return this;
    }

    public Builder username(Collection<String> username) {
      this.username = username;
      return this;
    }

    public Builder requestDateFrom(LocalDateTime requestDateFrom) {
      this.requestDateFrom = requestDateFrom;
      return this;
    }

    public Builder requestDateTo(LocalDateTime requestDateTo) {
      this.requestDateTo = requestDateTo;
      return this;
    }

    public Builder objectId(Collection<String> objectId) {
      this.objectId = objectId;
      return this;
    }

    public JsonRequestHistoryFilter build() {
      JsonRequestHistoryFilter jsonRequestHistoryFilter = new JsonRequestHistoryFilter();
      jsonRequestHistoryFilter.setClassCode(classCode);
      jsonRequestHistoryFilter.setUserId(userId);
      jsonRequestHistoryFilter.setUsername(username);
      jsonRequestHistoryFilter.setRequestDateFrom(requestDateFrom);
      jsonRequestHistoryFilter.setRequestDateTo(requestDateTo);
      jsonRequestHistoryFilter.setObjectId(objectId);
      return jsonRequestHistoryFilter;
    }
  }
}
