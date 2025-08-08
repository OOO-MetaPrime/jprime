package mp.jprime.metamaps.loaders.xml;

import org.springframework.core.io.Resource;

import java.util.Collection;

/**
 * Загрузка описание привязки меты к хранилищам из xml
 */
public interface JPMapsXmlResources {
  Collection<Resource> getResources();
}
