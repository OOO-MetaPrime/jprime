package mp.jprime.metamaps.xmlloader.services;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.metamaps.JPAttrMap;
import mp.jprime.metamaps.JPClassMap;
import mp.jprime.metamaps.JPMapsLoader;

import mp.jprime.metamaps.beans.JPAttrMapBean;
import mp.jprime.metamaps.beans.JPClassMapBean;
import mp.jprime.metamaps.xmlloader.beans.XmlJpAttrMap;
import mp.jprime.metamaps.xmlloader.beans.XmlJpAttrMaps;
import mp.jprime.metamaps.xmlloader.beans.XmlJpClassMap;
import mp.jprime.metamaps.xmlloader.beans.XmlJpClassMaps;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Загрузка описание привязки меты к хранилищам
 */
@Service
public class JPMapsXmlLoader implements JPMapsLoader {
  private final Collection<JPMapsXmlResources> resources;

  public JPMapsXmlLoader(@Autowired(required = false) Collection<JPMapsXmlResources> resources) {
    this.resources = resources;
  }

  /**
   * Вычитывает метаописание
   *
   * @return Список метаописания
   */
  @Override
  public Collection<JPClassMap> load() {
    if (resources == null || resources.isEmpty()) {
      return Collections.emptyList();
    }
    Collection<JPClassMap> result = new ArrayList<>();
    for (JPMapsXmlResources resource : resources) {
      result.addAll(xmlLoad(resource.getResources()));
    }
    return result;
  }

  private Collection<JPClassMap> xmlLoad(Collection<Resource> resources) {
    if (resources == null || resources.isEmpty()) {
      return Collections.emptyList();
    }
    try {
      Collection<JPClassMap> result = new ArrayList<>();
      for (Resource res : resources) {
        try (InputStream is = res.getInputStream()) {
          XmlJpClassMaps xmlJpClassMaps = new XmlMapper().readValue(is, XmlJpClassMaps.class);
          if (xmlJpClassMaps == null || xmlJpClassMaps.getJpClassMaps() == null) {
            continue;
          }
          for (XmlJpClassMap cls : xmlJpClassMaps.getJpClassMaps()) {
            XmlJpAttrMaps bAttrs = cls.getJpAttrMaps();
            XmlJpAttrMap[] attrs = bAttrs != null ? bAttrs.getJpAttrMaps() : null;
            if (attrs == null) {
              continue;
            }
            Collection<JPAttrMap> newAttrs = new ArrayList<>(attrs.length);
            for (XmlJpAttrMap attr : attrs) {
              newAttrs.add(JPAttrMapBean.newBuilder()
                  .code(attr.getCode())
                  .map(attr.getMap())
                  .fuzzyMap(attr.getFuzzyMap())
                  .cs(attr.getCs())
                  .readOnly(attr.getReadOnly())
                  .build());
            }
            JPClassMap newCls = JPClassMapBean.newBuilder()
                .code(cls.getCode())
                .storage(cls.getStorage())
                .map(cls.getMap())
                .schema(cls.getSchema())
                .attrs(newAttrs)
                .build();
            result.add(newCls);
          }
        }
      }
      return result;
    } catch (IOException e) {
      throw JPRuntimeException.wrapException(e);
    }
  }
}