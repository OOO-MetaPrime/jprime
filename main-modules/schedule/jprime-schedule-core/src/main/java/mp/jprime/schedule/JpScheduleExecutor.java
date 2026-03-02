package mp.jprime.schedule;

import mp.jprime.common.JPParam;
import mp.jprime.lang.JPMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Выполняющая логика
 */
public interface JpScheduleExecutor {
  Logger LOG = LoggerFactory.getLogger(JpScheduleExecutor.class);

  /**
   * Код планировщика
   *
   * @return Код
   */
  String getCode();

  /**
   * Название задачи
   *
   * @return Название
   */
  String getName();

  /**
   * Описание задачи
   *
   * @return Описание
   */
  String getDescription();

  /**
   * Параметры логики
   *
   * @return Список параметров
   */
  Collection<JPParam> getParams();

  /**
   * Выполнение операции
   *
   * @param paramValues Значения параметров
   */
  void execute(JPMap paramValues);
}
