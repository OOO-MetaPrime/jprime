package mp.jprime.io;

import org.springframework.core.io.Resource;

import java.util.Collection;

/**
 * Загрузка Resource
 */
public interface JpLoaderResources {
  Collection<Resource> getResources();
}
