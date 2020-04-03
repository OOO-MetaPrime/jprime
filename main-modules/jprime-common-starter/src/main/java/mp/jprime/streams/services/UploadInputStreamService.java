package mp.jprime.streams.services;

import mp.jprime.streams.UploadInputStream;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

/**
 * Работа с UploadInputStream
 */
public interface UploadInputStreamService {
  /**
   * Читаем файл
   *
   * @param filePart Файл
   * @return Читаем UploadInputStream
   */
  Mono<UploadInputStream> read(FilePart filePart);
}
