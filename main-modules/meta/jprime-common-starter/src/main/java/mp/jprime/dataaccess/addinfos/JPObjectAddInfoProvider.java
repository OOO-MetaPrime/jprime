package mp.jprime.dataaccess.addinfos;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

/**
 * Логика работы с дополнительными сведениями об объекте
 */
public interface JPObjectAddInfoProvider {
  /**
   * Возвращает сведения об объекте
   *
   * @param params Параметры для получения сведений
   */
  Collection<AddInfo> getAddInfo(JPObjectAddInfoParams params);

  /**
   * Возвращает сведения об объекте
   *
   * @param params Параметры для получения сведений
   */
  default Flux<AddInfo> getAsyncAddInfo(JPObjectAddInfoParams params) {
    return Mono.fromCallable(() -> getAddInfo(params))
        .flatMapMany(Flux::fromIterable);
  }
}
