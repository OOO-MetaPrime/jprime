package mp.jprime.meta.xmlloader.services;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPMetaLoader;
import mp.jprime.meta.beans.JPAttrBean;
import mp.jprime.meta.beans.JPClassBean;
import mp.jprime.meta.beans.JPImmutableClassBean;
import mp.jprime.meta.xmlloader.beans.XmlJpAttr;
import mp.jprime.meta.xmlloader.beans.XmlJpAttrs;
import mp.jprime.meta.xmlloader.beans.XmlJpClass;
import mp.jprime.meta.xmlloader.beans.XmlJpClasses;
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
 * Загрузка метаинформации из xml
 */
@Service
public class JPMetaXmlLoader implements JPMetaLoader {
  private static final Logger LOG = LoggerFactory.getLogger(JPMetaXmlLoader.class);
  /**
   * Папка с метаописанием
   */
  public static final String RESOURCES_FOLDER = "meta/";

  /**
   * Вычитывает метаописание
   *
   * @return Список метаописания
   */
  public Flux<JPClass> load() {
    return Flux.create(x -> {
      loadTo(x);
      x.complete();
    });
  }

  private void loadTo(FluxSink<JPClass> sink) {
    URL url = null;
    try {
      url = ResourceUtils.getURL("classpath:" + JPMetaXmlLoader.RESOURCES_FOLDER);
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
          XmlJpClasses xmlJpClasses = new XmlMapper().readValue(Files.newBufferedReader(entry), XmlJpClasses.class);
          if (xmlJpClasses == null || xmlJpClasses.getJpClasses() == null) {
            continue;
          }
          for (XmlJpClass cls : xmlJpClasses.getJpClasses()) {
            XmlJpAttrs bAttrs = cls.getJpAttrs();
            XmlJpAttr[] attrs = bAttrs != null ? bAttrs.getJpAttrs() : null;
            if (attrs == null) {
              continue;
            }
            Collection<JPAttr> newAttrs = new ArrayList<>(attrs.length);
            for (XmlJpAttr attr : attrs) {
              String name = attr.getName();
              String shortName = attr.getShortName();
              String descr = attr.getDescription();

              newAttrs.add(JPAttrBean.newBuilder()
                  .guid(attr.getGuid())
                  .type(attr.getType())
                  .length(attr.getLength())
                  .identifier(attr.isIdentifier())
                  .mandatory(attr.isMandatory())
                  .qName(attr.getqName())
                  .jpPackage(attr.getJpPackage())
                  .name(name != null ? name : descr)
                  .shortName(shortName)
                  .description(descr)
                  .code(attr.getCode())
                  .jpClassCode(cls.getGuid())
                  .refJpClassCode(attr.getRefJpClass())
                  .refJpAttrCode(attr.getRefJpAttr())
                  .virtualReference(attr.getVirtualReference())
                  .virtualType(attr.getVirtualType())
                  .build());
            }
            String name = cls.getName();
            String shortName = cls.getShortName();
            String descr = cls.getDescription();

            JPClass newCls = JPImmutableClassBean.newBuilder()
                .jpClass(JPClassBean.newBuilder()
                    .guid(cls.getGuid())
                    .code(cls.getCode())
                    .qName(cls.getqName())
                    .pluralCode(cls.getPluralCode())
                    .jpPackage(cls.getJpPackage())
                    .name(name != null ? name : descr)
                    .shortName(shortName)
                    .description(descr)
                    .shortName(cls.getShortName())
                    .description(cls.getDescription())
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
