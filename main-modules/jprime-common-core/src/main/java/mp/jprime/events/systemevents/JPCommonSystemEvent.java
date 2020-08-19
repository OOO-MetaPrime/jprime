package mp.jprime.events.systemevents;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

/**
 * Обобщенное системное событие
 */
public final class JPCommonSystemEvent implements JPSystemEvent {
  // Дата события
  private final LocalDateTime eventDate;
  // Код события
  private final String eventCode;
  // Признак внешнего события
  private final boolean external;
  // прочие данные
  private final Map<String, String> data;

  private JPCommonSystemEvent(LocalDateTime eventDate, String eventCode, Boolean external, Map<String, String> data) {
    this.eventDate = eventDate != null ? eventDate : LocalDateTime.now();
    this.eventCode = eventCode != null ? eventCode : "jpCommonSystemEvent";
    this.external = external != null ? external : false;
    this.data = Collections.unmodifiableMap(data == null ? Collections.emptyMap() : data);
  }

  @Override
  public LocalDateTime getEventDate() {
    return eventDate;
  }

  @Override
  public String getEventCode() {
    return eventCode;
  }

  @Override
  public boolean isExternal() {
    return external;
  }

  @Override
  public Map<String, String> getData() {
    return data;
  }

  /**
   * Построитель
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель JPCommonSystemEvent
   */
  public static final class Builder {
    // Дата события
    private LocalDateTime eventDate;
    // Код события
    private String eventCode;
    // Признак внешнего события
    private Boolean external;
    // прочие данные
    private Map<String, String> data;

    /**
     * Создаем JPCommonSystemEvent
     *
     * @return JPCommonSystemEvent
     */
    public JPCommonSystemEvent build() {
      return new JPCommonSystemEvent(
          eventDate, eventCode, external, data
      );
    }

    /**
     * Дата события
     *
     * @param eventDate Дата события
     * @return Builder
     */
    public Builder eventDate(LocalDateTime eventDate) {
      this.eventDate = eventDate;
      return this;
    }

    /**
     * Код события
     *
     * @param eventCode Код события
     * @return Builder
     */
    public Builder eventCode(String eventCode) {
      this.eventCode = eventCode;
      return this;
    }

    /**
     * Признак внешнего события
     *
     * @param external Признак внешнего события
     * @return Builder
     */
    public Builder external(Boolean external) {
      this.external = external;
      return this;
    }

    /**
     * Данные
     *
     * @param data Данные
     * @return Builder
     */
    public Builder data(Map<String, String> data) {
      this.data = data;
      return this;
    }
  }
}
