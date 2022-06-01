package mp.jprime.dataaccess.applyvalues;

import mp.jprime.dataaccess.beans.JPData;
import reactor.core.publisher.Mono;

/**
 * Логика для дополнения значений.
 * В ответе будут только данные, подлежащие изменению, входящая data в ответе не дублируется.
 */
public interface JPObjectApplyValueService {
  /**
   * Возвращает дополненные значения
   *
   * @param params Параметры для дополнения значений
   */
  JPData getApplyValues(JPObjectApplyValueParams params);

  /**
   * Возвращает дополненные значения
   *
   * @param params Параметры для дополнения значений
   */
  default Mono<JPData> getAsyncApplyValues(JPObjectApplyValueParams params) {
    return Mono.fromCallable(() -> getApplyValues(params));
  }
}