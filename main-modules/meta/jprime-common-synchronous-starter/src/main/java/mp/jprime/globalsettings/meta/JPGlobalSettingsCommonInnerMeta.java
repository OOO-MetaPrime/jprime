package mp.jprime.globalsettings.meta;

import mp.jprime.globalsettings.storage.JPGlobalSettingsStorage;
import mp.jprime.meta.annotations.JPAttr;
import mp.jprime.meta.annotations.JPClass;
import mp.jprime.meta.beans.JPType;
import mp.jprime.metamaps.annotations.JPAttrMap;
import mp.jprime.metamaps.annotations.JPClassMap;

@JPClass(
    code = JPGlobalSettingsCommonInnerMeta.CLASS_CODE,
    name = "Настройки системы",
    inner = true,
    attrs = {
        @JPAttr(
            code = JPGlobalSettingsCommonInnerMeta.Attr.CODE,
            type = JPType.STRING,
            identifier = true,
            mandatory = true,
            name = "Идентификатор настройки"
        ),
        @JPAttr(
            code = JPGlobalSettingsCommonInnerMeta.Attr.PROPERTY,
            type = JPType.JSON,
            name = "Описание настройки"
        ),
        @JPAttr(
            code = JPGlobalSettingsCommonInnerMeta.Attr.VALUE,
            type = JPType.JSON,
            name = "Значение настройки"
        )
    }
)
@JPClassMap(
    code = JPGlobalSettingsCommonInnerMeta.CLASS_CODE,
    storage = JPGlobalSettingsStorage.COMMON_CODE,
    schema = JPGlobalSettingsStorage.SCHEMA,
    map = "jp_globalsettings",
    attrs = {
        @JPAttrMap(
            code = JPGlobalSettingsCommonInnerMeta.Attr.CODE,
            map = "code"
        ),
        @JPAttrMap(
            code = JPGlobalSettingsCommonInnerMeta.Attr.PROPERTY,
            map = "property"
        ),
        @JPAttrMap(
            code = JPGlobalSettingsCommonInnerMeta.Attr.VALUE,
            map = "value"
        )
    }
)
/*
 * Настройки системы
 */
public class JPGlobalSettingsCommonInnerMeta extends JPGlobalSettingsBaseInnerMeta {
  /**
   * Кодовое имя класса
   */
  public static final String CLASS_CODE = "jpCommonGlobalSetting";
}
