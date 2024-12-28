package mp.jprime.meta.services;

import mp.jprime.security.AuthInfo;
import reactor.core.publisher.Mono;

import java.util.Collection;

/**
 * Удаленное хранилище метаинформации
 */
public interface JPMetaRemoteStorage {
  /**
   * Возвращает кодовые имена классов
   *
   * @param auth AuthInfo
   * @return кодовые имена классов
   */
  Mono<Collection<String>> getJPClassCodeList(AuthInfo auth);
}
