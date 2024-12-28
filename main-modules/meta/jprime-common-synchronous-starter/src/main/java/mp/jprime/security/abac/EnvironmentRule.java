package mp.jprime.security.abac;

import mp.jprime.dataaccess.conds.CollectionCond;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

/**
 * Правило - настройки среды
 */
public interface EnvironmentRule extends Rule {
  /**
   * Дни
   *
   * @return Условие на дни
   */
  Collection<DayOfWeek> getDaysOfWeek();

  /**
   * Время начала
   *
   * @return Время начала
   */
  LocalTime getFromTime();

  /**
   * Время окончания
   *
   * @return Время окончания
   */
  LocalTime getToTime();

  /**
   * Дата-время начала
   *
   * @return Дата-время начала
   */
  LocalDateTime getFromDateTime();

  /**
   * Дата-время окончания
   *
   * @return Дата-время окончания
   */
  LocalDateTime getToDateTime();

  /**
   * Возвращает условие на IP
   *
   * @return Условие на IP
   */
  CollectionCond<String> getIpCond();
}
