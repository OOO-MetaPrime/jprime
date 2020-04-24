package mp.jprime.events.stream;

import java.time.LocalDateTime;

/**
 * Данные события
 *
 * @param <T> Тип данных события
 */
public class JPEventBaseInfo<T extends JsonEvent> implements JPEventInfo<T> {
  private String eventType;
  private LocalDateTime date;
  private T eventData;

  private JPEventBaseInfo(String eventType, LocalDateTime date, T eventData) {
    this.eventType = eventType;
    this.date = date;
    this.eventData = eventData;
  }

  /**
   * Дата события
   *
   * @return Дата события
   */
  public LocalDateTime getDate() {
    return date;
  }

  /**
   * Тип события
   *
   * @return Тип события
   */
  @Override
  public String getEventType() {
    return eventType;
  }

  /**
   * Данные события
   *
   * @return Данные события
   */
  @Override
  public T getEventData() {
    return eventData;
  }

  /**
   * Построитель JPEventBaseInfo
   *
   * @return Builder
   */
  public static <T extends JsonEvent> Builder<T> newBuilder() {
    return new Builder<>();
  }

  /**
   * Построитель JPEventBaseInfo
   */
  public static final class Builder<T extends JsonEvent> {
    private String eventType;
    private LocalDateTime date;
    private T eventData;


    private Builder() {
    }

    /**
     * Дата события
     *
     * @param date Дата события
     * @return Builder
     */
    public Builder<T> date(LocalDateTime date) {
      this.date = date;
      return this;
    }

    /**
     * Тип события
     *
     * @param eventType Тип события
     * @return Builder
     */
    public Builder<T> eventType(String eventType) {
      this.eventType = eventType;
      return this;
    }

    /**
     * Данные события
     *
     * @param eventData Данные события
     * @return Builder
     */
    public Builder<T> eventData(T eventData) {
      this.eventData = eventData;
      return this;
    }

    /**
     * Создаем Event
     *
     * @return Event
     */
    public JPEventBaseInfo<T> build() {
      return new JPEventBaseInfo<>(eventType, date, eventData);
    }
  }
}
