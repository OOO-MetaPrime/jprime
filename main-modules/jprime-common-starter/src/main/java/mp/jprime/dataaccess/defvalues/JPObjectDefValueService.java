package mp.jprime.dataaccess.defvalues;

import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.security.AuthInfo;
import reactor.core.publisher.Mono;

/**
 * Логика вычисления значений по-умолчанию
 */
public interface JPObjectDefValueService {
  /**
   * Возвращает значения по-умолчанию
   *
   * @param jpClassCode Кодовое имя класса объекта для расчета значений по-умолчанию
   * @param params      Параметры для вычисления значений по-умолчанию
   */
  JPMutableData getDefValues(String jpClassCode, JPObjectDefValueParams params);

  /**
   * Возвращает значения по-умолчанию
   *
   * @param jpClassCode Кодовое имя класса объекта для расчета значений по-умолчанию
   * @param params      Параметры для вычисления значений по-умолчанию
   */
  default Mono<JPMutableData> getAsyncDefValues(String jpClassCode, JPObjectDefValueParams params) {
    return Mono.fromCallable(() -> getDefValues(jpClassCode, params));
  }


  /**
   * Возвращает значения по-умолчанию
   *
   * @param jpClassCode Кодовое имя класса объекта для расчета значений по-умолчанию
   * @param auth        Данные авторизации
   */
  default JPMutableData getDefValues(String jpClassCode, AuthInfo auth) {
    return getDefValues(jpClassCode, JPObjectDefValueParamsBean.newBuilder().auth(auth).build());
  }

  /**
   * Возвращает значения по-умолчанию
   *
   * @param jpClassCode Кодовое имя класса объекта для расчета значений по-умолчанию
   * @param auth        Данные авторизации
   */
  default Mono<JPMutableData> getAsyncDefValues(String jpClassCode, AuthInfo auth) {
    return Mono.fromCallable(() -> getDefValues(jpClassCode, auth));
  }
}