package mp.jprime.imex.rules.beans;

import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.imex.rules.JPMapRules;
import mp.jprime.meta.beans.JPType;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Настройки правил мапинга для связывания со столбцом по имени
 */
public class JPMapRulesNamedBean implements JPMapRules<String> {
  private final Map<String, JPColumnSettings> rules;

  private JPMapRulesNamedBean(Map<String, JPMapRules.JPColumnSettings> rules) {
    if (MapUtils.isEmpty(rules)) {
      throw new JPRuntimeException("jp.imex.rulesnamedbean.rules.empty", "Не заданы правила");
    }

    this.rules = Map.copyOf(rules);
  }

  /**
   * Определяет тип параметра класса
   *
   * @return Параметр класса
   */
  @Override
  public JPType getKeyType() {
    return JPType.STRING;
  }

  /**
   * Имена столбцов
   *
   * @return Имена столбцов
   */
  @Override
  public Collection<String> getColumns() {
    return rules.keySet();
  }

  /**
   * Настройки по столбцу
   *
   * @param columnName Имя столбца
   * @return Настройки по столбцу
   */
  @Override
  public JPMapRules.JPColumnSettings getSettings(String columnName) {
    return rules.get(columnName);
  }

  /**
   * Создаёт построитель правил {@link JPMapRulesNamedBean.Builder}
   *
   * @return Построитель правил {@link JPMapRulesNamedBean.Builder}
   */
  public static JPMapRulesNamedBean.Builder newBuilder() {
    return new JPMapRulesNamedBean.Builder();
  }

  /**
   * Построитель атрибута
   */
  public static class AttrBuilder {
    private final JPMapRulesNamedBean.Builder builder;

    private final JPMapRulesNamedBean.ColumnBuilder columnBuilder;

    private AttrBuilder(JPMapRulesNamedBean.Builder builder, JPMapRulesNamedBean.ColumnBuilder columnBuilder) {
      this.builder = builder;
      this.columnBuilder = columnBuilder;
    }

    /**
     * Добавляет атрибут в колонку
     *
     * @param attrName Имя атрибута
     * @param type     Тип атрибута
     * @return Построитель атрибута {@link JPMapRulesNamedBean.AttrBuilder}
     */
    public JPMapRulesNamedBean.AttrBuilder addAttr(String attrName, JPType type) {
      return columnBuilder.addAttr(attrName, type);
    }

    /**
     * Создает новые правила для именнованных столбцов
     *
     * @return Правила для именнованных колонок {@link JPMapRulesNamedBean}
     */
    public JPMapRulesNamedBean build() {
      return builder.build();
    }

    /**
     * Добавляет колонку
     *
     * @param columnName Имя колонки
     * @param mandatory  Обязательность атрибута
     * @return Построитель колонки {@link JPMapRulesNamedBean.ColumnBuilder}
     */
    public JPMapRulesNamedBean.ColumnBuilder addColumn(String columnName, boolean mandatory) {
      return builder.addColumn(columnName, mandatory);
    }
  }

  /**
   * Построитель колонки
   */
  public static class ColumnBuilder {

    private final Map<String, JPType> attrSettings;

    private final boolean mandatory;

    private final JPMapRulesNamedBean.Builder builder;

    private ColumnBuilder(JPMapRulesNamedBean.Builder builder, boolean mandatory) {
      this.builder = builder;
      this.mandatory = mandatory;
      this.attrSettings = new HashMap<>();
    }

    /**
     * Добавляет атрибут в колонку
     *
     * @param attrName Имя атрибута
     * @param type     Тип атрибута
     * @return Построитель атрибута {@link JPMapRulesNamedBean.AttrBuilder}
     */
    public JPMapRulesNamedBean.AttrBuilder addAttr(String attrName, JPType type) {
      if (StringUtils.isBlank(attrName)) {
        throw new JPRuntimeException("jp.imex.rulesnamedbean.attrName.empty", "Не задано имя атрибута");
      }

      if (type == null) {
        throw new JPRuntimeException("jp.imex.rulesnamedbean.type.empty", "Не задан тип атрибута");
      }

      attrSettings.put(attrName, type);
      return new JPMapRulesNamedBean.AttrBuilder(builder, this);
    }

    private Map<String, JPType> getAttrMap() {
      return attrSettings;
    }
  }

  /**
   * Построитель {@link JPMapRulesNamedBean}
   */
  public static class Builder {
    private final Map<String, JPMapRulesNamedBean.ColumnBuilder> rules;

    private Builder() {
      rules = new HashMap<>();
    }

    /**
     * Добавляет колонку
     *
     * @param columnName Имя колонки
     * @param mandatory  Обязательность атрибута
     * @return Построитель колонки {@link JPMapRulesNamedBean.ColumnBuilder}
     */
    public JPMapRulesNamedBean.ColumnBuilder addColumn(String columnName, boolean mandatory) {
      if (StringUtils.isBlank(columnName)) {
        throw new JPRuntimeException("jp.imex.rulesnamedbean.columnName.empty", "Не задано имя колонки");
      }

      JPMapRulesNamedBean.ColumnBuilder columnBuilder = new JPMapRulesNamedBean.ColumnBuilder(this, mandatory);
      rules.put(columnName, columnBuilder);
      return columnBuilder;
    }

    /**
     * Создает новые правила для именнованных столбцов
     *
     * @return Правила для именнованных колонок {@link JPMapRulesNamedBean}
     */
    public JPMapRulesNamedBean build() {
      return new JPMapRulesNamedBean(rules.entrySet()
          .stream()
          .collect(Collectors.toMap(Map.Entry::getKey, y -> JPColumnSettingsBean.of(y.getValue().getAttrMap(), y.getValue().mandatory)))
      );
    }
  }
}
