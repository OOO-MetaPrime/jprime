package mp.jprime.repositories;

import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.files.JPIdFileInfo;
import mp.jprime.security.AuthInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

/**
 * Данные файлов объекта
 */
public interface JPFileLoader {
  /**
   * Данные файла
   *
   * @param classCode Кодовое имя класса
   * @param filter    Условие выборки
   * @param attr      Атрибут типа файл
   * @param auth      AuthInfo
   * @return {@link JPIdFileInfo}
   */
  Collection<JPIdFileInfo> getInfos(String classCode, Filter filter, String attr, AuthInfo auth);


  /**
   * Данные файла
   *
   * @param classCode Кодовое имя класса
   * @param filter    Условие выборки
   * @param attr      Атрибут типа файл
   * @return {@link JPIdFileInfo}
   */
  Collection<JPIdFileInfo> getInfos(String classCode, Filter filter, String attr);

  /**
   * Данные файла
   *
   * @param classCode Кодовое имя класса
   * @param filter    Условие выборки
   * @param attr      Атрибут типа файл
   * @param auth      AuthInfo
   * @return {@link JPIdFileInfo}
   */
  default Flux<JPIdFileInfo> asyncGetInfos(String classCode, Filter filter, String attr, AuthInfo auth) {
    return Flux.fromIterable(getInfos(classCode, filter, attr, auth));
  }

  /**
   * Данные файла
   *
   * @param id   Идентификатор объекта
   * @param attr Атрибут типа файл
   * @param auth AuthInfo
   * @return {@link JPIdFileInfo}
   */
  default JPIdFileInfo getInfo(JPId id, String attr, AuthInfo auth) {
    return getInfo(id, null, attr, auth);
  }

  /**
   * Данные файла
   *
   * @param id   Идентификатор объекта
   * @param attr Атрибут типа файл
   * @param auth AuthInfo
   * @return FileInfo
   */
  default Mono<JPIdFileInfo> asyncGetInfo(JPId id, String attr, AuthInfo auth) {
    return Mono.fromCallable(() -> getInfo(id, attr, auth));
  }

  /**
   * Данные файла
   *
   * @param id     Идентификатор объекта
   * @param filter Условие выборки
   * @param attr   Атрибут типа файл
   * @param auth   AuthInfo
   * @return FileInfo
   */
  JPIdFileInfo getInfo(JPId id, Filter filter, String attr, AuthInfo auth);

  /**
   * Данные файла
   *
   * @param id     Идентификатор объекта
   * @param filter Условие выборки
   * @param attr   Атрибут типа файл
   * @param auth   AuthInfo
   * @return FileInfo
   */
  default Mono<JPIdFileInfo> asyncGetInfo(JPId id, Filter filter, String attr, AuthInfo auth) {
    return Mono.fromCallable(() -> getInfo(id, filter, attr, auth));
  }
}
