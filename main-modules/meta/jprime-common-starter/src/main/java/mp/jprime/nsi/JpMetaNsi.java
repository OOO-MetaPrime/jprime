package mp.jprime.nsi;

import java.util.Collection;
import java.util.Map;

/**
 * Описание НСИ справочника, обертка над обычной метой
 */
public interface JpMetaNsi<T extends Comparable<T>> extends JpNsiType<T> {
  /**
   * Приводит тип НСИ к указанному
   *
   * @return JpMetaNsi<Integer>
   */
  default JpMetaNsi<Integer> toIntegerKeyType() {
    return (JpMetaNsi<Integer>) this;
  }

  /**
   * Приводит тип НСИ к указанному
   *
   * @return JpMetaNsi<String>
   */
  default JpMetaNsi<String> toStringKeyType() {
    return (JpMetaNsi<String>) this;
  }

  /**
   * Мета класс
   *
   * @return Кодовое имя класса
   */
  String getJpClass();

  /**
   * Атрибут, являющийся кодом
   *
   * @return Кодовое имя атрибута
   */
  String getIdAttr();

  /**
   * Атрибут, являющийся названием
   *
   * @return Кодовое имя атрибута
   */
  String getNameAttr();

  /**
   * Атрибут, являющийся настройкой доступа ОКТМО
   *
   * @return Кодовое имя атрибута
   */
  String getOktmoAccessAttr();

  /**
   * Атрибут, являющийся настройкой доступа по предметным группам
   *
   * @return Кодовое имя атрибута
   */
  String getSubjectGroupAccessAttr();

  /**
   * Список полей для поиска
   *
   * @return Список полей для поиска
   */
  Collection<String> getSearchProperties();

  /**
   * Маппинг атрибутов
   *
   * @return Маппинг
   */
  Map<String, String> getPropertiesMap();
}
