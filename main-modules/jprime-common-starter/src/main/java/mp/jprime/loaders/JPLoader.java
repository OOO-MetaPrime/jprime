package mp.jprime.loaders;

import mp.jprime.exceptions.JPRuntimeException;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.Collections;

/**
 * Логика базовых загрузчиков
 */
public interface JPLoader<T> {
  /**
   * Читает данные
   * @return данные
   */
  Flux<T> load();

  default Path toPath(URL url) {
    try {
      String[] array = url.toString().split("!");
      if (array.length == 1) {
        return Paths.get(url.toURI());
      } else {
        FileSystem fs;
        try {
          fs = FileSystems.newFileSystem(URI.create(array[0]), Collections.emptyMap());
        } catch (Exception e) {
          fs = FileSystems.getFileSystem(URI.create(array[0]));
        }
        if (fs == null) {
          return null;
        }
        return array.length > 2 ? fs.getPath(array[1], array[2]) : fs.getPath(array[1]);
      }
    } catch (URISyntaxException e) {
      throw JPRuntimeException.wrapException(e);
    }
  }
}