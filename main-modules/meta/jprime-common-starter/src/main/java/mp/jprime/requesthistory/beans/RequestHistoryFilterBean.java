package mp.jprime.requesthistory.beans;

import mp.jprime.requesthistory.RequestHistoryFilter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Запрос на поиск истории запросов
 */
public class RequestHistoryFilterBean implements RequestHistoryFilter {
  private final Collection<String> classCode;
  private final Collection<String> userId;
  private final Collection<String> username;
  private final LocalDateTime requestDateFrom;
  private final LocalDateTime requestDateTo;
  private final Collection<String> objectId;

  /**
   * Конструктор Запроса на поиск истории запросов
   *
   * @param classCode       Код класса
   * @param userId          Идентификатор пользователя
   * @param username        Логин пользователя
   * @param requestDateFrom Дата запроса с
   * @param requestDateTo   Дата запроса по
   * @param objectId        Идентификатор объекта
   */
  private RequestHistoryFilterBean(Collection<String> classCode, Collection<String> userId, Collection<String> username, LocalDateTime requestDateFrom, LocalDateTime requestDateTo, Collection<String> objectId) {
    this.classCode = classCode == null ? Collections.emptyList() : Collections.unmodifiableCollection(new ArrayList<>(classCode));
    this.userId = userId == null ? Collections.emptyList() : Collections.unmodifiableCollection(new ArrayList<>(userId));
    this.username = username == null ? Collections.emptyList() : Collections.unmodifiableCollection(new ArrayList<>(username));
    this.requestDateFrom = requestDateFrom;
    this.requestDateTo = requestDateTo;
    this.objectId = objectId == null ? Collections.emptyList() : Collections.unmodifiableCollection(new ArrayList<>(objectId));
  }

  /**
   * Получить Код класса
   *
   * @return Код класса
   */
  @Override
  public Collection<String> getClassCode() {
    return classCode;
  }

  /**
   * Получить Идентификатор пользователя
   *
   * @return Идентификатор пользователя
   */
  @Override
  public Collection<String> getUserId() {
    return userId;
  }

  /**
   * Получить Логин пользователя
   *
   * @return Логин пользователя
   */
  @Override
  public Collection<String> getUsername() {
    return username;
  }

  /**
   * Получить Дату запроса с
   *
   * @return Дата запроса с
   */
  @Override
  public LocalDateTime getRequestDateFrom() {
    return requestDateFrom;
  }

  /**
   * Получить Дату запроса по
   *
   * @return Дата запроса по
   */
  @Override
  public LocalDateTime getRequestDateTo() {
    return requestDateTo;
  }

  /**
   * Получить Идентификатор объекта
   *
   * @return Идентификатор объекта
   */
  @Override
  public Collection<String> getObjectId() {
    return objectId;
  }

  /**
   * Получить Построитель запроса на поиск истории запросов
   *
   * @return Запрос на поиск истории запросов
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель запроса на поиск истории запросов
   */
  public static final class Builder {
    private Collection<String> classCode;
    private Collection<String> userId;
    private Collection<String> username;
    private LocalDateTime requestDateFrom;
    private LocalDateTime requestDateTo;
    private Collection<String> objectId;

    private Builder() {
    }

    /**
     * Установить код класса
     *
     * @param classCode Код класса
     * @return {@link Builder}
     */
    public Builder classCode(Collection<String> classCode) {
      this.classCode = classCode;
      return this;
    }

    /**
     * Установить идентификатор пользователя
     *
     * @param userId Идентификатор пользователя
     * @return {@link Builder}
     */
    public Builder userId(Collection<String> userId) {
      this.userId = userId;
      return this;
    }

    /**
     * Установить логин пользователя
     *
     * @param username Логин пользователя
     * @return {@link Builder}
     */
    public Builder username(Collection<String> username) {
      this.username = username;
      return this;
    }

    /**
     * Установить дату запроса с
     *
     * @param requestDateFrom Дата запроса с
     * @return {@link Builder}
     */
    public Builder requestDateFrom(LocalDateTime requestDateFrom) {
      this.requestDateFrom = requestDateFrom;
      return this;
    }

    /**
     * Установить дату запроса по
     *
     * @param requestDateTo Дата запроса по
     * @return {@link Builder}
     */
    public Builder requestDateTo(LocalDateTime requestDateTo) {
      this.requestDateTo = requestDateTo;
      return this;
    }

    /**
     * Установить идентификатор объекта
     *
     * @param objectId Идентификатор объекта
     * @return {@link Builder}
     */
    public Builder objectId(Collection<String> objectId) {
      this.objectId = objectId;
      return this;
    }

    /**
     * Собрать запрос на поиск истории запросов
     *
     * @return Запрос на поиск истории запросов
     */
    public RequestHistoryFilterBean build() {
      return new RequestHistoryFilterBean(classCode, userId, username, requestDateFrom, requestDateTo, objectId);
    }
  }
}
