package mp.jprime.security.abac.beans;

import mp.jprime.dataaccess.conds.CollectionCond;
import mp.jprime.security.abac.EnvironmentRule;
import mp.jprime.security.beans.JPAccessType;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;

/**
 * Правило - настройки среды
 */
public class EnvironmentRuleBean implements EnvironmentRule {
  private final String name;
  private final String qName;
  private final JPAccessType effect;
  private final Collection<DayOfWeek> daysOfWeek;
  private final LocalTime fromTime;
  private final LocalTime toTime;
  private final LocalDateTime fromDateTime;
  private final LocalDateTime toDateTime;
  private final CollectionCond<String> ip;

  private EnvironmentRuleBean(String name, String qName, JPAccessType effect,
                              Collection<DayOfWeek> daysOfWeek, LocalTime fromTime, LocalTime toTime,
                              LocalDateTime fromDateTime, LocalDateTime toDateTime,
                              CollectionCond<String> ip) {
    this.name = name;
    this.qName = qName;
    this.effect = effect;
    this.daysOfWeek = daysOfWeek != null ? Collections.unmodifiableCollection(daysOfWeek) : Collections.emptyList();
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
  @Override
  public String getName() {
    return name;
  }

  /**
   * QName правила
   *
   * @return qName правила
   */
  @Override
  public String getQName() {
    return qName;
  }

  /**
   * Возвращает тип доступа (разрешительный/запретительный)
   *
   * @return Разрешение/Запрет
   */
  @Override
  public JPAccessType getEffect() {
    return effect;
  }

  /**
   * Возвращает условие на IP
   *
   * @return Условие на IP
   */
  @Override
  public CollectionCond<String> getIpCond() {
    return ip;
  }

  /**
   * Дни
   *
   * @return Условие на дни
   */
  @Override
  public Collection<DayOfWeek> getDaysOfWeek() {
    return daysOfWeek;
  }

  /**
   * Время начала
   *
   * @return Время начала
   */
  @Override
  public LocalTime getFromTime() {
    return fromTime;
  }

  /**
   * Время окончания
   *
   * @return Время окончания
   */
  @Override
  public LocalTime getToTime() {
    return toTime;
  }

  /**
   * Дата-время начала
   *
   * @return Дата-время начала
   */
  @Override
  public LocalDateTime getFromDateTime() {
    return fromDateTime;
  }

  /**
   * Дата-время окончания
   *
   * @return Дата-время окончания
   */
  @Override
  public LocalDateTime getToDateTime() {
    return toDateTime;
  }

  public static Builder newBuilder(String name, JPAccessType effect) {
    return new Builder(name, effect);
  }

  public static final class Builder {
    private String name;
    private String qName;
    private JPAccessType effect;
    private Collection<DayOfWeek> daysOfWeek;
    private LocalTime fromTime;
    private LocalTime toTime;
    private LocalDateTime fromDateTime;
    private LocalDateTime toDateTime;
    private CollectionCond<String> ip;

    private Builder(String name, JPAccessType effect) {
      this.name = name;
      this.effect = effect;
    }

    public EnvironmentRuleBean build() {
      return new EnvironmentRuleBean(name, qName, effect, daysOfWeek, fromTime, toTime, fromDateTime, toDateTime, ip);
    }

    public Builder qName(String qName) {
      this.qName = qName;
      return this;
    }

    public Builder daysOfWeek(Collection<DayOfWeek> daysOfWeek) {
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

    public Builder ip(CollectionCond<String> ip) {
      this.ip = ip;
      return this;
    }
  }
}
