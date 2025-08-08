package mp.jprime.repositories;

import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.dataaccess.params.JPUpdate;
import mp.jprime.files.JPFileCommonInfo;
import mp.jprime.reactor.core.publisher.JPMono;
import reactor.core.publisher.Mono;

import java.io.InputStream;

/**
 * Загрузка файлов объекта
 */
public interface JPFileUploader {
  /**
   * Загружает файл и сохраняет данные в JPFileInfo
   *
   * @param storageCode Код хранилища
   * @param storagePath Путь в хранилище
   * @param fileName    Имя файла
   * @param is          InputStream
   * @return JPFileInfo
   */
  JPFileCommonInfo upload(String storageCode, String storagePath, String fileName, InputStream is);

  /**
   * Загружает файл и сохраняет данные в JPCreate.Builder
   *
   * @param builder  JPCreate.Builder
   * @param attr     Атрибут типа файл
   * @param fileName Имя файла
   * @param is       InputStream
   * @return JPCreate.Builder
   */
  JPCreate.Builder upload(JPCreate.Builder builder, String attr, String fileName, InputStream is);

  /**
   * Загружает файл и сохраняет данные в JPCreate.Builder
   *
   * @param builder  JPCreate.Builder
   * @param attr     Атрибут типа файл
   * @param fileName Имя файла
   * @param is       InputStream
   * @return JPCreate.Builder
   */
  default Mono<JPCreate.Builder> asyncUpload(JPCreate.Builder builder, String attr, String fileName, InputStream is) {
    return JPMono.fromCallable(() -> upload(builder, attr, fileName, is));
  }

  /**
   * Загружает файл и сохраняет данные в JPUpdate.Builder
   *
   * @param builder  JPUpdate.Builder
   * @param attr     Атрибут типа файл
   * @param fileName Имя файла
   * @param is       InputStream
   * @return JPUpdate.Builder
   */
  JPUpdate.Builder upload(JPUpdate.Builder builder, String attr, String fileName, InputStream is);

  /**
   * Загружает файл и сохраняет данные в JPUpdate.Builder
   *
   * @param builder  JPUpdate.Builder
   * @param attr     Атрибут типа файл
   * @param fileName Имя файла
   * @param is       InputStream
   * @return JPUpdate.Builder
   */
  default Mono<JPUpdate.Builder> asyncUpload(JPUpdate.Builder builder, String attr, String fileName, InputStream is) {
    return JPMono.fromCallable(() -> upload(builder, attr, fileName, is));
  }

  /**
   * Заменяет ключевые слова в пути, если они присутствуют
   *
   * @param path Путь
   * @return Измененный путь
   */
  String getStoragePath(String path);
}
