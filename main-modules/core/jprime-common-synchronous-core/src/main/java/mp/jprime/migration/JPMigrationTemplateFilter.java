package mp.jprime.migration;

/**
 * Шаблонные значения для миграции
 */
public interface JPMigrationTemplateFilter {
  /**
   * Возвращает шаблон
   *
   * @return Шаблон
   */
  String getTemplate();

  /**
   * Возвращает описание шаблона
   *
   * @return описание
   */
  default String getPattern() {
    return "{" + getTemplate() + "}";
  }

  /**
   * Форматируем шаблонное значение
   *
   * @return Значение поля
   */
  String getValue();
}
