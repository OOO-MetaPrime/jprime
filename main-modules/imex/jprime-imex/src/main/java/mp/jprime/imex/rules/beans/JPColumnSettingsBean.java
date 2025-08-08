package mp.jprime.imex.rules.beans;

import mp.jprime.imex.rules.JPMapRules;
import mp.jprime.meta.beans.JPType;

import java.util.Collection;
import java.util.Map;

/**
 * Настройки чтения столбца
 */
public class JPColumnSettingsBean implements JPMapRules.JPColumnSettings {
  private final Map<String, JPType> attrSettings;

  private final boolean mandatory;

  private JPColumnSettingsBean(Map<String, JPType> attrSettings, boolean mandatory) {
    this.attrSettings = Map.copyOf(attrSettings);
    this.mandatory = mandatory;
  }

  /**
   * Создает настройки чтения столбца {@link JPColumnSettingsBean}
   *
   * @param attrSettings Атрибут и тип данных
   * @param mandatory    Признак обязательности
   */
  public static JPColumnSettingsBean of(Map<String, JPType> attrSettings, boolean mandatory) {
    return new JPColumnSettingsBean(attrSettings, mandatory);
  }

  /**
   * Имена атрибутов
   *
   * @return Имена атрибутов
   */
  @Override
  public Collection<String> getAttrs() {
    return attrSettings.keySet();
  }

  /**
   * Тип атрибута
   *
   * @return Тип атрибута
   */
  @Override
  public JPType getAttrType(String attrName) {
    return attrSettings.get(attrName);
  }

  /**
   * Признак обязательности
   *
   * @return Признак обязательности
   */
  @Override
  public boolean isMandatory() {
    return mandatory;
  }
}
