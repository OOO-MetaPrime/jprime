package mp.jprime.json.versioning;

import mp.jprime.lang.JPJsonNode;

import java.util.Collection;

/**
 * Конвертация json из одного типа в другой
 */
public interface JPJsonVersionConverter<T> {
  /**
   * Код группы версий
   *
   * @return Код группы версий
   */
  String getGroupCode();

  /**
   * Текущая версия
   *
   * @return Текущая версия
   */
  Integer getVersion();

  /**
   * Класс для сериализации
   *
   * @return Класс для сериализации
   */
  Class<T> getBeanClass();

  /**
   * Поддерживаемые версии
   *
   * @return Поддерживаемые версии
   */
  Collection<Integer> fromVersions();

  /**
   * Конвертация JPJsonNode
   *
   * @param version Исходная версия
   * @param value   JPJsonNode
   * @return JPJsonNode
   */
  JPJsonNode convertToNode(Integer version, JPJsonNode value);
}
