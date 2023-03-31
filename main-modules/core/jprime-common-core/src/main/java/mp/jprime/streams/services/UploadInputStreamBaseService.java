package mp.jprime.streams.services;

import mp.jprime.streams.UploadInputStream;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Работа с UploadInputStream
 */
@Service
public class UploadInputStreamBaseService implements UploadInputStreamService {
  /**
   * Читаем файл
   *
   * @param filePart Файл
   * @return Читаем UploadInputStream
   */
  public Mono<UploadInputStream> read(FilePart filePart) {
    return Mono.fromCallable(() -> new UploadInputStream(filePart.filename())
        .collectInputStream(
            readAsInputStream(filePart.content())
        )
    );
  }

  private InputStream readAsInputStream(Flux<DataBuffer> buffers) {
    try {
      PipedOutputStream osPipe = new PipedOutputStream();
      PipedInputStream isPipe = new PipedInputStream(osPipe);

      DataBufferUtils.write(buffers, osPipe)
          .subscribeOn(Schedulers.boundedElastic())
          .doOnComplete(() -> {
            try {
              osPipe.close();
            } catch (IOException ignored) {
            }
          })
          .subscribe(DataBufferUtils.releaseConsumer());

      return isPipe;
    } catch (Exception e) {
      return null;
    }
  }
}
