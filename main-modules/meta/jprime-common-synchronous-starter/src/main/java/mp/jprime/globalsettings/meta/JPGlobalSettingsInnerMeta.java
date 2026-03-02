package mp.jprime.globalsettings.meta;

import mp.jprime.globalsettings.storage.JPGlobalSettingsStorage;
import mp.jprime.meta.annotations.JPAttr;
import mp.jprime.meta.annotations.JPClass;
import mp.jprime.meta.beans.JPType;
import mp.jprime.metamaps.annotations.JPAttrMap;
import mp.jprime.metamaps.annotations.JPClassMap;

@JPClass(
    code = JPGlobalSettingsInnerMeta.CLASS_CODE,
    name = "Настройки системы",
    inner = true,
    attrs = {
        @JPAttr(
            code = JPGlobalSettingsInnerMeta.Attr.CODE,
            type = JPType.STRING,
            identifier = true,
            mandatory = true,
            name = "Идентификатор настройки"
        ),
        @JPAttr(
            code = JPGlobalSettingsInnerMeta.Attr.PROPERTY,
            type = JPType.JSON,
            name = "Описание настройки"
        ),
        @JPAttr(
            code = JPGlobalSettingsInnerMeta.Attr.VALUE,
            type = JPType.JSON,
            name = "Значение настройки"
        )
    }
)
@JPClassMap(
    code = JPGlobalSettingsInnerMeta.CLASS_CODE,
    storage = JPGlobalSettingsStorage.CODE,
    schema = JPGlobalSettingsStorage.SCHEMA,
    map = "jp_globalsettings",
    attrs = {
        @JPAttrMap(
            code = JPGlobalSettingsInnerMeta.Attr.CODE,
            map = "code"
        ),
        @JPAttrMap(
            code = JPGlobalSettingsInnerMeta.Attr.PROPERTY,
            map = "property"
        ),
        @JPAttrMap(
            code = JPGlobalSettingsInnerMeta.Attr.VALUE,
            map = "value"
        )
    }
)
/*
 * Настройки системы
 */
public class JPGlobalSettingsInnerMeta extends JPGlobalSettingsBaseInnerMeta {
  /**
   * Кодовое имя класса
   */
  public static final String CLASS_CODE = "jpGlobalSetting";
}
