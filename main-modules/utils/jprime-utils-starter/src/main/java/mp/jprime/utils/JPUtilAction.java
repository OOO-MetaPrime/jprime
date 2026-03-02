package mp.jprime.utils;

import mp.jprime.security.AuthInfo;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Признак обработчика утилиты
 */
public interface JPUtilAction {
  /**
   * Проверка доступа
   *
   * @param auth Доступ
   * @return Да/Нет
   */
  default boolean checkAccess(AuthInfo auth) {
    return true;
  }

  /**
   * Основная логика шага утилиты
   *
   * @param inParams MapInParams
   * @param swe      ServerWebExchange
   * @param auth     AuthInfo
   * @return Ответ утилиты
   */
  <T> Mono<JPUtilOutParams<T>> exec(String mode, JPUtilInParams inParams, ServerWebExchange swe, AuthInfo auth);
}
