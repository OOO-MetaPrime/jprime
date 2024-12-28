package mp.jprime.dataaccess.synchronizers;

import mp.jprime.security.AuthInfo;

import java.util.Collection;

/**
 * Базовый интерфейс для синхронизации данных
 *
 * @param <T> Тип первичного ключа класса
 */
public interface JPSynchronizerRepository<T> {
  /**
   * Синхронизация настроек
   * Перекидывает данные из {@code mutable} в настоящие таблицы
   */
  void synchronize();

  /**
   * Синхронизация настроек по пользователю
   * Перекидывает данные из {@code mutable} в настоящие таблицы
   *
   * @param auth AuthInfo
   */
  void synchronize(AuthInfo auth);

  /**
   * Синхронизация настроек по идентификаторам
   * Перекидывает данные из {@code mutable} в настоящие таблицы
   *
   * @param guid Идентификаторы
   */
  void synchronize(Collection<T> guid);
}
