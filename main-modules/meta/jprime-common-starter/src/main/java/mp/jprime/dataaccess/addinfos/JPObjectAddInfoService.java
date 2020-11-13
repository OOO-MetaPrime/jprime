package mp.jprime.dataaccess.addinfos;

import mp.jprime.security.AuthInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

/**
 * Логика работы с дополнительными сведениями об объекте
 */
public interface JPObjectAddInfoService {
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


  /**
   * Возвращает сведения об объекте
   *
   * @param jpClassCode Кодовое имя класса объекта
   * @param id          Идентификатор объекта
   * @param auth        Данные авторизации
   */
  default Collection<AddInfo> getAddInfo(String jpClassCode, Object id, AuthInfo auth) {
    return getAddInfo(JPObjectAddInfoParamsBean.newBuilder(jpClassCode, id).auth(auth).build());
  }

  /**
   * Возвращает сведения об объекте
   *
   * @param jpClassCode Кодовое имя класса объекта
   * @param id          Идентификатор объекта
   * @param auth        Данные авторизации
   */
  default Flux<AddInfo> getAsyncAddInfo(String jpClassCode, Object id, AuthInfo auth) {
    return Mono.fromCallable(() -> getAddInfo(jpClassCode, id, auth))
        .flatMapMany(Flux::fromIterable);
  }
}
