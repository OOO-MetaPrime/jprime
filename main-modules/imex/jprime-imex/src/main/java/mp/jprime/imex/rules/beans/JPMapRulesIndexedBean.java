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
 * Настройки правил мапинга для связывания со столбцом по индексу
 */
public class JPMapRulesIndexedBean implements JPMapRules<Integer> {
  private final Map<Integer, JPMapRules.JPColumnSettings> rules;

  private JPMapRulesIndexedBean(Map<Integer, JPMapRules.JPColumnSettings> rules) {
    if (MapUtils.isEmpty(rules)) {
      throw new JPRuntimeException("jp.imex.rulesindexedbean.rules.empty", "Не заданы правила");
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
    return JPType.INT;
  }

  /**
   * Индексы столбцов
   *
   * @return Индексы столбцов
   */
  @Override
  public Collection<Integer> getColumns() {
    return rules.keySet();
  }

  /**
   * Настройки по индексу столбца
   *
   * @param index Индекс столбца
   * @return Настройки по индексу столбца
   */
  @Override
  public JPMapRules.JPColumnSettings getSettings(Integer index) {
    return rules.get(index);
  }

  /**
   * Создаёт построитель правил
   *
   * @return Построитель правил {@link JPMapRulesIndexedBean.Builder}
   */
  public static JPMapRulesIndexedBean.Builder newBuilder() {
    return new JPMapRulesIndexedBean.Builder();
  }

  /**
   * Построитель атрибута
   */
  public static class AttrBuilder {
    private final JPMapRulesIndexedBean.Builder builder;

    private final JPMapRulesIndexedBean.ColumnBuilder columnBuilder;

    private AttrBuilder(JPMapRulesIndexedBean.Builder builder, JPMapRulesIndexedBean.ColumnBuilder columnBuilder) {
      this.builder = builder;
      this.columnBuilder = columnBuilder;
    }

    /**
     * Добавляет атрибут в колонку
     *
     * @param attrName Имя атрибута
     * @param type     Тип атрибута
     * @return Построитель атрибута {@link JPMapRulesIndexedBean.AttrBuilder}
     */
    public JPMapRulesIndexedBean.AttrBuilder addAttr(String attrName, JPType type) {
      return columnBuilder.addAttr(attrName, type);
    }

    /**
     * Создает новые правила для именнованных столбцов
     *
     * @return Правила для именнованных колонок {@link JPMapRulesIndexedBean}
     */
    public JPMapRulesIndexedBean build() {
      return builder.build();
    }

    /**
     * Добавляет колонку
     *
     * @param index     Индекс колонки
     * @param mandatory Обязательность атрибута
     * @return Построитель колонки {@link JPMapRulesIndexedBean.ColumnBuilder}
     */
    public JPMapRulesIndexedBean.ColumnBuilder addColumn(int index, boolean mandatory) {
      return builder.addColumn(index, mandatory);
    }
  }

  /**
   * Построитель колонки
   */
  public static class ColumnBuilder {

    private final Map<String, JPType> attrSettings;

    private final boolean mandatory;

    private final JPMapRulesIndexedBean.Builder builder;

    private ColumnBuilder(JPMapRulesIndexedBean.Builder builder, boolean mandatory) {
      this.builder = builder;
      this.mandatory = mandatory;
      this.attrSettings = new HashMap<>();
    }

    /**
     * Добавляет атрибут в колонку
     *
     * @param attrName Имя атрибута
     * @param type     Тип атрибута
     * @return Построитель атрибута {@link JPMapRulesIndexedBean.AttrBuilder}
     */
    public JPMapRulesIndexedBean.AttrBuilder addAttr(String attrName, JPType type) {
      if (StringUtils.isBlank(attrName)) {
        throw new JPRuntimeException("jp.imex.rulesindexedbean.attrName.empty", "Не задано имя атрибута");
      }

      if (type == null) {
        throw new JPRuntimeException("jp.imex.rulesindexedbean.type.empty", "Не задан тип атрибута");
      }

      attrSettings.put(attrName, type);
      return new AttrBuilder(builder, this);
    }

    private Map<String, JPType> getAttrMap() {
      return attrSettings;
    }
  }

  /**
   * Построитель {@link JPMapRulesIndexedBean}
   */
  public static class Builder {
    private final Map<Integer, JPMapRulesIndexedBean.ColumnBuilder> rules;

    private Builder() {
      rules = new HashMap<>();
    }

    /**
     * Добавляет колонку
     *
     * @param index     Индекс колонки
     * @param mandatory Обязательность атрибута
     * @return Построитель колонки {@link JPMapRulesIndexedBean.ColumnBuilder}
     */
    public JPMapRulesIndexedBean.ColumnBuilder addColumn(int index, boolean mandatory) {
      if (index < 0) {
        throw new JPRuntimeException("jp.imex.rulesindexedbean.index.invalid", "Индекс столбца не может быть отрицательным");
      }

      ColumnBuilder columnBuilder = new ColumnBuilder(this, mandatory);
      rules.put(index, columnBuilder);
      return columnBuilder;
    }

    /**
     * Создает новые правила для связываемых столбцов по индексу
     *
     * @return Правила для колонок по индексу {@link JPMapRulesIndexedBean}
     */
    public JPMapRulesIndexedBean build() {
      return new JPMapRulesIndexedBean(rules.entrySet()
          .stream()
          .collect(Collectors.toMap(Map.Entry::getKey, y -> JPColumnSettingsBean.of(y.getValue().getAttrMap(), y.getValue().mandatory)))
      );
    }
  }
}
