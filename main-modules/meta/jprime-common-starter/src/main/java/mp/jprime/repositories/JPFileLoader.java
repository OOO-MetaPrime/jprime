package mp.jprime.repositories;

import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.files.JPFileInfo;
import mp.jprime.files.JPIdFileInfo;
import mp.jprime.reactor.core.publisher.JPMono;
import mp.jprime.security.AuthInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
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
    return JPMono.fromCallable(() -> getInfos(classCode, filter, attr, auth))
        .flatMapMany(Flux::fromIterable);
  }

  /**
   * Данные файла
   *
   * @param id   Идентификатор объекта
   * @param attr Атрибут типа файл
   * @return FileInfo
   */
  default Mono<JPIdFileInfo> asyncGetInfo(JPId id, String attr) {
    return JPMono.fromCallable(() -> getInfo(id, attr, null));
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
    return JPMono.fromCallable(() -> getInfo(id, attr, auth));
  }

  /**
   * Данные файла
   *
   * @param id   Идентификатор объекта
   * @param attr Атрибут типа файл
   * @return FileInfo
   */
  JPIdFileInfo getInfo(JPId id, String attr);

  /**
   * Данные файла
   *
   * @param id   Идентификатор объекта
   * @param attr Атрибут типа файл
   * @param auth AuthInfo
   * @return FileInfo
   */
  default JPIdFileInfo getInfo(JPId id, String attr, AuthInfo auth) {
    return getInfo(id, null, attr, auth);
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
   * @param id   Идентификатор объекта
   * @param attr Атрибут типа файл
   * @param auth AuthInfo
   * @return {@link JPIdFileInfo}
   */
  default InputStream getInputStream(JPId id, String attr, AuthInfo auth) {
    return getInputStream(getInfo(id, attr, auth));
  }

  /**
   * Содержимое файла файла
   *
   * @param info Данные файла
   * @return FileInfo
   */
  InputStream getInputStream(JPFileInfo<?> info);

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
    return JPMono.fromCallable(() -> getInfo(id, filter, attr, auth));
  }
}
