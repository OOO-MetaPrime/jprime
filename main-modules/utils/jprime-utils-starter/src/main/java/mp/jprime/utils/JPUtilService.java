package mp.jprime.utils;

import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.security.AuthInfo;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

/**
 * Сервис работы с утилитами
 */
public interface JPUtilService {
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
   * Возвращает метод утилиты
   *
   * @param utilCode   Код утилиты
   * @param methodCode Код метода
   * @return метод утилиты
   */
  JPUtilMode getMode(String utilCode, String methodCode);

  /**
   * Возвращает все утилиты
   *
   * @return Утилиты
   */
  Flux<? extends JPUtilMode> getUtils();

  /**
   * Возвращает все утилиты, доступные данной авторизации
   *
   * @param auth Данные авторизации
   * @return Утилиты
   */
  Flux<? extends JPUtilMode> getUtils(AuthInfo auth);

  /**
   * Возвращает все утилиты для указанного класса, доступные данной авторизации
   *
   * @param className Код метакласса
   * @param auth      Данные авторизации
   * @return Утилиты
   */
  Flux<? extends JPUtilMode> getUtils(String className, AuthInfo auth);

  /**
   * Выполняет метод утилиты
   *
   * @param utilCode   Код утилиты
   * @param methodCode Код метода
   * @param auth       Данные авторизации
   * @return Исходящие параметры
   */
  Mono<? extends JPUtilMode> apply(String utilCode, String methodCode, AuthInfo auth);

  /**
   * Выполняет метод утилиты
   *
   * @param mode Шаг утилиты
   * @param in   Входящие параметры
   * @param swe  ServerWebExchange
   * @param auth Данные авторизации
   * @return Исходящие параметры
   */
  Mono<JPUtilOutParams> apply(JPUtilMode mode, JPUtilInParams in, ServerWebExchange swe, AuthInfo auth);

  /**
   * Выполняет метод утилиты
   *
   * @param utilCode   Код утилиты
   * @param methodCode Код метода
   * @param in         Входящие параметры
   * @param swe        ServerWebExchange
   * @param auth       Данные авторизации
   * @return Исходящие параметры
   */
  Mono<JPUtilOutParams> apply(String utilCode, String methodCode, JPUtilInParams in, ServerWebExchange swe, AuthInfo auth);

  /**
   * Выполняет метод утилиты
   *
   * @param utilCode   Код утилиты
   * @param methodCode Код метода
   * @param in         Входящие параметры
   * @param swe        ServerWebExchange
   * @param auth       Данные авторизации
   * @return Исходящие параметры
   */
  default Mono<JPUtilCheckOutParams> check(String utilCode, String methodCode, JPUtilInParams in, ServerWebExchange swe, AuthInfo auth) {
    return apply(utilCode, methodCode, in, swe, auth)
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
   * @param auth       Данные авторизации
   * @return Исходящие параметры
   */
  default Mono<JPUtilOutParams> apply(String utilCode, String methodCode, JPUtilInParams in, AuthInfo auth) {
    return apply(utilCode, methodCode, in, null, auth);
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