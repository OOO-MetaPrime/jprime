package mp.jprime.metamaps.xmlloader.services;

import org.springframework.core.io.Resource;

import java.util.Collection;

/**
 * Загрузка описание привязки меты к хранилищам из xml
 */
public interface JPMapsXmlResources {
  Collection<Resource> getResources();
}
