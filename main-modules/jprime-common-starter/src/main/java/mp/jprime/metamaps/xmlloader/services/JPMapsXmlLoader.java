package mp.jprime.metamaps.xmlloader.services;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.metamaps.JPAttrMap;
import mp.jprime.metamaps.JPClassMap;
import mp.jprime.metamaps.JPMapsLoader;
import mp.jprime.metamaps.beans.JPAttrMapBean;
import mp.jprime.metamaps.beans.JPClassMapBean;
import mp.jprime.metamaps.beans.JPImmutableClassMapBean;
import mp.jprime.metamaps.xmlloader.beans.XmlJpAttrMap;
import mp.jprime.metamaps.xmlloader.beans.XmlJpAttrMaps;
import mp.jprime.metamaps.xmlloader.beans.XmlJpClassMap;
import mp.jprime.metamaps.xmlloader.beans.XmlJpClassMaps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Загрузка описание привязки меты к хранилищам
 */
@Service
public class JPMapsXmlLoader implements JPMapsLoader {
  private static final Logger LOG = LoggerFactory.getLogger(JPMapsXmlLoader.class);
  /**
   * Папка с описанием привязки меты к хранилищам
   */
  public static final String RESOURCES_FOLDER = "metamaps/";

  /**
   * Вычитывает описание привязки меты к хранилищам
   *
   * @return Список описания привязки меты к хранилищам
   */
  public Flux<JPClassMap> load() {
    return Flux.create(x -> {
      loadTo(x);
      x.complete();
    });
  }

  private void loadTo(FluxSink<JPClassMap> sink) {
    URL url = null;
    try {
      url = ResourceUtils.getURL("classpath:" + JPMapsXmlLoader.RESOURCES_FOLDER);
    } catch (FileNotFoundException e) {
      LOG.debug(e.getMessage(), e);
    }
    if (url == null) {
      return;
    }
    try {
      Path path = toPath(url);
      if (path == null || !Files.exists(path)) {
        return;
      }
      try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
        for (Path entry : stream) {
          XmlJpClassMaps xmlJpClassMaps = new XmlMapper().readValue(Files.newBufferedReader(entry), XmlJpClassMaps.class);
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
            JPClassMap newCls = JPImmutableClassMapBean.newBuilder()
                .jpClassMap(
                    JPClassMapBean.newBuilder()
                        .code(cls.getCode())
                        .storage(cls.getStorage())
                        .map(cls.getMap())
                        .attrs(newAttrs)
                        .build()
                )
                .build();
            sink.next(newCls);
          }
        }
      }
    } catch (IOException e) {
      throw JPRuntimeException.wrapException(e);
    }
  }
}