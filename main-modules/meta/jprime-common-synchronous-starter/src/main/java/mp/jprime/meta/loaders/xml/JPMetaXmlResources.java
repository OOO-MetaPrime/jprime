package mp.jprime.meta.loaders.xml;

import org.springframework.core.io.Resource;

import java.util.Collection;

/**
 * Загрузка метаинформации из xml
 */
public interface JPMetaXmlResources {
  Collection<Resource> getResources();
}
