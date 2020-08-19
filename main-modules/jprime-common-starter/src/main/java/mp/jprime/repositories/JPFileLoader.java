package mp.jprime.repositories;

import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.files.JPFileInfo;
import mp.jprime.security.AuthInfo;
import reactor.core.publisher.Mono;

/**
 * Данные файлов объекта
 */
public interface JPFileLoader {
  /**
   * Данные файла
   *
   * @param id   Идентификатор объекта
   * @param attr Атрибут типа файл
   * @param auth AuthInfo
   * @return JFileInfo
   */
  JPFileInfo getInfo(JPId id, String attr, AuthInfo auth);

  /**
   * Данные файла
   *
   * @param id   Идентификатор объекта
   * @param attr Атрибут типа файл
   * @param auth AuthInfo
   * @return FileInfo
   */
  default Mono<JPFileInfo> asyncGetInfo(JPId id, String attr, AuthInfo auth) {
    return Mono.fromCallable(() -> getInfo(id, attr, auth));
  }
}
