package mp.jprime.meta.xmlloader.services;

import org.springframework.core.io.Resource;

import java.util.Collection;

/**
 * Загрузка метаинформации из xml
 */
public interface JPMetaXmlResources {
  Collection<Resource> getResources();
}
