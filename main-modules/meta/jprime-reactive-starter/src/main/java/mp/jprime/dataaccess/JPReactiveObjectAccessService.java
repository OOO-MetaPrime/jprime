package mp.jprime.dataaccess;

import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.security.AuthInfo;
import reactor.core.publisher.Mono;

/**
 * Интерфейс проверки доступа к объекту
 */
public interface JPReactiveObjectAccessService {
  /**
   * Проверка доступа на создание
   *
   * @param classCode Код метаописания
   * @param auth      AuthInfo
   * @return Да/Нет
   */
  Mono<Boolean> checkCreate(String classCode, AuthInfo auth);

  /**
   * Проверка доступа на чтение
   *
   * @param id   Идентификатор объекта
   * @param auth AuthInfo
   * @return Да/Нет
   */
  Mono<Boolean> checkRead(JPId id, AuthInfo auth);

  /**
   * Проверка доступа на удаление
   *
   * @param id   Идентификатор объекта
   * @param auth AuthInfo
   * @return Да/Нет
   */
  Mono<Boolean> checkDelete(JPId id, AuthInfo auth);

  /**
   * Проверка доступа на обновление
   *
   * @param id   Идентификатор объекта
   * @param auth AuthInfo
   * @return Да/Нет
   */
  Mono<Boolean> checkUpdate(JPId id, AuthInfo auth);

  /**
   * Проверка доступа на чтение + наличие объекта
   *
   * @param id   Идентификатор объекта
   * @param auth AuthInfo
   * @return Да/Нет
   */
  Mono<Boolean> checkReadExists(JPId id, AuthInfo auth);

  /**
   * Проверка доступа на удаление
   *
   * @param id   Идентификатор объекта + наличие объекта
   * @param auth AuthInfo
   * @return Да/Нет
   */
  Mono<Boolean> checkDeleteExists(JPId id, AuthInfo auth);

  /**
   * Проверка доступа на обновление
   *
   * @param id   Идентификатор объекта + наличие объекта
   * @param auth AuthInfo
   * @return Да/Нет
   */
  Mono<Boolean> checkUpdateExists(JPId id, AuthInfo auth);
}
