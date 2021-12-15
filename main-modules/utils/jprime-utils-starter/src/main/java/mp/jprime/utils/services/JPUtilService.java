package mp.jprime.utils.services;

import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.security.AuthInfo;
import mp.jprime.utils.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

/**
 * Сервис работы с утилитами
 */
public interface JPUtilService {
  /**
   * Режим проверки доступности по умолчанию
   */
  String CHECK_MODE = "check";

  /**
   * Возвращает список всех кодовых имен утилит
   *
   * @return список всех кодовых имен утилит
   */
  Collection<String> getUtilCodes();

  /**
   * Возвращает список всех кодовых имен общих утилит
   *
   * @return список всех кодовых имен утилит
   */
  Collection<String> getUniUtilCodes();

  /**
   * Возвращает утилиту по ее коду
   *
   * @param utilCode Код утилиты
   * @return Утилита
   */
  JPUtil getUtil(String utilCode);

  /**
   * Возвращает все утилиты
   *
   * @return Утилиты
   */
  Flux<JPUtilMode> getUtils();

  /**
   * Возвращает все утилиты, доступные данной авторизации
   *
   * @param authInfo Данные авторизации
   * @return Утилиты
   */
  Flux<JPUtilMode> getUtils(AuthInfo authInfo);

  /**
   * Возвращает все утилиты для указанного класса, доступные данной авторизации
   *
   * @param className Код метакласса
   * @param authInfo  Данные авторизации
   * @return Утилиты
   */
  Flux<JPUtilMode> getUtils(String className, AuthInfo authInfo);

  /**
   * Выполняет метод утилиты
   *
   * @param utilCode   Код утилиты
   * @param methodCode Код метода
   * @param authInfo   Данные авторизации
   * @return Исходящие параметры
   */
  Mono<? extends JPUtilMode> apply(String utilCode, String methodCode, AuthInfo authInfo);

  /**
   * Выполняет метод утилиты
   *
   * @param mode     Шаг утилиты
   * @param in       Входящие параметры
   * @param swe      ServerWebExchange
   * @param authInfo Данные авторизации
   * @return Исходящие параметры
   */
  Mono<JPUtilOutParams> apply(JPUtilMode mode, JPUtilInParams in, ServerWebExchange swe, AuthInfo authInfo);

  /**
   * Выполняет метод утилиты
   *
   * @param utilCode   Код утилиты
   * @param methodCode Код метода
   * @param in         Входящие параметры
   * @param swe        ServerWebExchange
   * @param authInfo   Данные авторизации
   * @return Исходящие параметры
   */
  Mono<JPUtilOutParams> apply(String utilCode, String methodCode, JPUtilInParams in, ServerWebExchange swe, AuthInfo authInfo);

  /**
   * Выполняет метод утилиты
   *
   * @param utilCode   Код утилиты
   * @param methodCode Код метода
   * @param in         Входящие параметры
   * @param swe        ServerWebExchange
   * @param authInfo   Данные авторизации
   * @return Исходящие параметры
   */
  default Mono<JPUtilCheckOutParams> check(String utilCode, String methodCode, JPUtilInParams in, ServerWebExchange swe, AuthInfo authInfo) {
    return apply(utilCode, methodCode, in, swe, authInfo)
        .cast(JPUtilCheckOutParams.class)
        .onErrorResume(JPRuntimeException.class, e -> Mono.just(JPUtilCheckOutParams.newBuilder()
            .denied(true)
            .qName(e.getMessageCode())
            .description(e.getMessage())
            .build()
        ))
        .onErrorResume(e -> Mono.just(JPUtilCheckOutParams.newBuilder()
            .denied(true)
            .qName(e.getMessage())
            .description(e.getMessage())
            .build()
        ));
  }

  /**
   * Выполняет метод утилиты
   *
   * @param utilCode   Код утилиты
   * @param methodCode Код метода
   * @param in         Входящие параметры
   * @param authInfo   Данные авторизации
   * @return Исходящие параметры
   */
  default Mono<JPUtilOutParams> apply(String utilCode, String methodCode, JPUtilInParams in, AuthInfo authInfo) {
    return apply(utilCode, methodCode, in, null, authInfo);
  }

  /**
   * Выполняет метод утилиты
   *
   * @param utilCode   Код утилиты
   * @param methodCode Код метода
   * @param in         Входящие параметры
   * @return Исходящие параметры
   */
  default Mono<JPUtilOutParams> apply(String utilCode, String methodCode, JPUtilInParams in) {
    return apply(utilCode, methodCode, in, null);
  }
}