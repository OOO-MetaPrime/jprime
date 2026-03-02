package mp.jprime.schedule.beans;

import mp.jprime.schedule.JpScheduleTaskCatalog;

/**
 * Каталог задач, выполняемых по расписанию
 */
public final class JpScheduleTaskCatalogBean implements JpScheduleTaskCatalog {
  /**
   * Код каталога
   */
  private final String catalog;
  /**
   * Название каталога
   */
  private final String name;
  /**
   * Признак временной возможности стоп/старт задач в этом каталоге
   */
  private final boolean tempIntermittent;

  private JpScheduleTaskCatalogBean(String catalog, String name, boolean tempIntermittent) {
    this.catalog = catalog;
    this.name = name;
    this.tempIntermittent = tempIntermittent;
  }

  public static JpScheduleTaskCatalog of(String catalog, String name) {
    return of(catalog, name, false);
  }

  public static JpScheduleTaskCatalog of(String catalog, String name, boolean tempIntermittent) {
    return new JpScheduleTaskCatalogBean(catalog, name, tempIntermittent);
  }

  @Override
  public String getCatalog() {
    return catalog;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isTempIntermittent() {
    return tempIntermittent;
  }
}
