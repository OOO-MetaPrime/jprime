package mp.jprime.security.abac.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;

/**
 * Правило - настройки среды
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonAbacEnvironmentRule {
  private String name;
  private String qName;
  private String effect;
  private Collection<String> daysOfWeek;
  private LocalTime fromTime;
  private LocalTime toTime;
  private LocalDateTime fromDateTime;
  private LocalDateTime toDateTime;
  private JsonAbacCond ip;

  public JsonAbacEnvironmentRule() {

  }

  private JsonAbacEnvironmentRule(String name, String qName, String effect,
                                  Collection<String> daysOfWeek, LocalTime fromTime, LocalTime toTime,
                                  LocalDateTime fromDateTime, LocalDateTime toDateTime,
                                  JsonAbacCond ip) {
    this.name = name;
    this.qName = qName;
    this.effect = effect;
    this.daysOfWeek = Collections.unmodifiableCollection(daysOfWeek != null ? daysOfWeek : Collections.emptyList());
    this.fromTime = fromTime;
    this.toTime = toTime;
    this.fromDateTime = fromDateTime;
    this.toDateTime = toDateTime;
    this.ip = ip;
  }

  /**
   * Название правила
   *
   * @return Название правила
   */
  public String getName() {
    return name;
  }

  /**
   * QName правила
   *
   * @return qName правила
   */
  public String getqName() {
    return qName;
  }

  /**
   * Возвращает тип доступа (разрешительный/запретительный)
   *
   * @return Разрешение/Запрет
   */
  public String getEffect() {
    return effect;
  }

  /**
   * Возвращает условие на IP
   *
   * @return Условие на IP
   */
  public JsonAbacCond getIp() {
    return ip;
  }

  /**
   * Дни
   *
   * @return Условие на дни
   */
  public Collection<String> getDaysOfWeek() {
    return daysOfWeek;
  }

  /**
   * Время начала
   *
   * @return Время начала
   */
  public LocalTime getFromTime() {
    return fromTime;
  }

  /**
   * Время окончания
   *
   * @return Время окончания
   */
  public LocalTime getToTime() {
    return toTime;
  }

  /**
   * Дата-время начала
   *
   * @return Дата-время начала
   */
  public LocalDateTime getFromDateTime() {
    return fromDateTime;
  }

  /**
   * Дата-время окончания
   *
   * @return Дата-время окончания
   */
  public LocalDateTime getToDateTime() {
    return toDateTime;
  }

  public static Builder newBuilder(String name, String effect) {
    return new Builder(name, effect);
  }

  public static final class Builder {
    private String name;
    private String qName;
    private String effect;
    private Collection<String> daysOfWeek;
    private LocalTime fromTime;
    private LocalTime toTime;
    private LocalDateTime fromDateTime;
    private LocalDateTime toDateTime;
    private JsonAbacCond ip;

    private Builder(String name, String effect) {
      this.name = name;
      this.effect = effect;
    }

    public JsonAbacEnvironmentRule build() {
      return new JsonAbacEnvironmentRule(name, qName, effect, daysOfWeek, fromTime, toTime, fromDateTime, toDateTime, ip);
    }

    public Builder qName(String qName) {
      this.qName = qName;
      return this;
    }

    public Builder daysOfWeek(Collection<String> daysOfWeek) {
      this.daysOfWeek = daysOfWeek;
      return this;
    }

    public Builder fromTime(LocalTime fromTime) {
      this.fromTime = fromTime;
      return this;
    }

    public Builder toTime(LocalTime toTime) {
      this.toTime = toTime;
      return this;
    }

    public Builder fromDateTime(LocalDateTime fromDateTime) {
      this.fromDateTime = fromDateTime;
      return this;
    }

    public Builder toDateTime(LocalDateTime toDateTime) {
      this.toDateTime = toDateTime;
      return this;
    }

    public Builder ip(JsonAbacCond ip) {
      this.ip = ip;
      return this;
    }
  }
}
