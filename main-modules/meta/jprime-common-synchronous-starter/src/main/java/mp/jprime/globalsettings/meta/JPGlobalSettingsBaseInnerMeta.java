package mp.jprime.globalsettings.meta;

import mp.jprime.meta.JPMeta;

public abstract class JPGlobalSettingsBaseInnerMeta extends JPMeta {
  /**
   * Атрибутивный состав
   */
  public interface Attr extends JPMeta.Attr {
    /**
     * Идентификатор настройки
     */
    String CODE = "code";
    /**
     * Описание настройки
     */
    String PROPERTY = "property";
    /**
     * Значение настройки
     */
    String VALUE = "value";
  }
}
