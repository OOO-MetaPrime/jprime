package mp.jprime.utils;

import mp.jprime.security.AuthInfo;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Базовая логика JPUtilAction
 */
public abstract class JPUtiBaseAction implements JPUtilAction {
  @Override
  public <T> Mono<JPUtilOutParams<T>> exec(String mode, JPUtilInParams inParams, ServerWebExchange swe, AuthInfo auth) {
    if (JPUtil.Mode.CHECK_MODE.equals(mode)) {
      return (Mono) check(inParams, swe, auth);
    } else if (JPUtil.Mode.IN_PARAMS_DEF_VALUES.equals(mode)) {
      return (Mono) inParamsDefValues(inParams, swe, auth);
    }
    return execMode(mode, inParams, swe, auth);
  }

  /**
   * Логика проверки возможности запуска утилиты
   *
   * @param inParams DefaultInParams
   * @param swe      ServerWebExchange
   * @param auth     AuthInfo
   * @return Результат проверки
   */
  protected Mono<JPUtilCheckOutParams> check(JPUtilInParams inParams, ServerWebExchange swe, AuthInfo auth) {
    return Mono.just(JPUtilCheckOutParams.ALLOW);
  }

  /**
   * Получение значений по умолчанию
   *
   * @param inParams DefaultInParams
   * @param swe      ServerWebExchange
   * @param auth     AuthInfo
   * @return Значения по умолчанию
   */
  protected Mono<JPUtilDefValuesOutParams> inParamsDefValues(JPUtilInParams inParams, ServerWebExchange swe, AuthInfo auth) {
    return Mono.just(JPUtilDefValuesOutParams.newBuilder().build());
  }

  /**
   * Основная логика шага утилиты
   *
   * @param modeCode Код шага утилиты
   * @param inParams MapInParams
   * @param swe      ServerWebExchange
   * @param auth     AuthInfo
   * @return Ответ утилиты
   */
  abstract protected <T> Mono<JPUtilOutParams<T>> execMode(String modeCode, JPUtilInParams inParams, ServerWebExchange swe, AuthInfo auth);
}
