package mp.jprime.metamaps.services;

import mp.jprime.exceptions.JPClassMapNotFoundException;
import mp.jprime.meta.JPClass;
import mp.jprime.metamaps.JPClassMap;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.util.Collection;

/**
 * Описания привязки метаинформации к хранилищу
 */
public interface JPMapsStorage {
  /**
   * Возвращает описание привязки метаинформации к хранилищу
   *
   * @param jpClass метакласс
   * @return описание привязки метаинформации к хранилищу
   */
  Mono<JPClassMap> mono(@NonNull JPClass jpClass);

  /**
   * Возвращает описание привязки метаинформации к хранилищу
   *
   * @param jpClass метакласс
   * @return описание привязки метаинформации к хранилищу
   */
  JPClassMap get(@NonNull JPClass jpClass);

  /**
   * Возвращает описание привязки метаинформации к хранилищу
   *
   * @param jpClass метакласс
   * @return описание привязки метаинформации к хранилищу
   */
  default JPClassMap getOrThrow(@NonNull JPClass jpClass) {
    JPClassMap jpClassMap = get(jpClass);
    if (jpClassMap == null) {
      throw new JPClassMapNotFoundException(jpClass.getCode());
    }
    return jpClassMap;
  }

  /**
   * Возвращает все описания привязки метаинформации к хранилищу
   *
   * @param jpClass метакласс
   * @return описания привязки метаинформации к хранилищу
   */
  Collection<JPClassMap> getAll(@NonNull JPClass jpClass);
}
