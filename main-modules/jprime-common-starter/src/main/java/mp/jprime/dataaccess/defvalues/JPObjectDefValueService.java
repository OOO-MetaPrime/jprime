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
   * @param authInfo    Данные авторизации
   */
  default JPMutableData getDefValues(String jpClassCode, AuthInfo authInfo) {
    return getDefValues(jpClassCode, JPObjectDefValueParamsBean.newBuilder().auth(authInfo).build());
  }

  /**
   * Возвращает значения по-умолчанию
   *
   * @param jpClassCode Кодовое имя класса объекта для расчета значений по-умолчанию
   * @param authInfo    Данные авторизации
   */
  default Mono<JPMutableData> getAsyncDefValues(String jpClassCode, AuthInfo authInfo) {
    return Mono.fromCallable(() -> getDefValues(jpClassCode, authInfo));
  }
}