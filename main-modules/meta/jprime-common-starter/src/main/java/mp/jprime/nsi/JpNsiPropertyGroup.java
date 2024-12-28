package mp.jprime.nsi;

import java.util.Collection;

/**
 * Группировка доп. свойств справочника НСИ
 */
public interface JpNsiPropertyGroup {
  /**
   * Название группы
   *
   * @return Название группы
   */
  String getGroupName();

  /**
   * Список дополнительных свойств
   *
   * @return Дополнительные свойства
   */
  Collection<String> getProperties();
}
