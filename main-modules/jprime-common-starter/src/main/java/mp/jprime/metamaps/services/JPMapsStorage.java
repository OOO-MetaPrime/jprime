package mp.jprime.metamaps.services;

import mp.jprime.meta.JPClass;
import mp.jprime.metamaps.JPClassMap;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

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
}
