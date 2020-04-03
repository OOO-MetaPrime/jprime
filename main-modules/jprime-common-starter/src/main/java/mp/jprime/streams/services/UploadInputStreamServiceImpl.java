package mp.jprime.streams.services;

import mp.jprime.streams.UploadInputStream;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Работа с UploadInputStream
 */
@Service
public class UploadInputStreamServiceImpl implements UploadInputStreamService {
  /**
   * Читаем файл
   *
   * @param filePart Файл
   * @return Читаем UploadInputStream
   */
  public Mono<UploadInputStream> read(FilePart filePart) {
    return filePart
        .content()
        .collect(() -> new UploadInputStream(filePart.filename()), (t, dataBuffer) -> t.collectInputStream(dataBuffer.asInputStream()));
  }
}
