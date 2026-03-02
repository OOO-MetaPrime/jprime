package mp.jprime.schedule;

/**
 * Каталог задач, выполняемых по расписанию
 */
public interface JpScheduleTaskCatalog {
  /**
   * Ядро системы
   */
  String JP = "jp";
  /**
   * Общесистемные задачи
   */
  String SYSTEM = "jp.system";

  /**
   * Код каталога
   *
   * @return Код каталога
   */
  String getCatalog();

  /**
   * Название каталога
   *
   * @return Название
   */
  String getName();

  /**
   * Признак временной возможности стоп/старт задач в этом каталоге
   *
   * @return Да/Нет
   */
  boolean isTempIntermittent();
}
