package mp.jprime.security.xmlloader.services;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.security.JPSecurityLoader;
import mp.jprime.security.JPSecurityPackage;
import mp.jprime.security.beans.JPAccess;
import mp.jprime.security.beans.JPImmutableSecurityPackageBean;
import mp.jprime.security.beans.JPSecurityPackageBean;
import mp.jprime.security.xmlloader.beans.*;
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

/**
 * Загрузка настроек доступа к пакету
 */
@Service
public class JPSecurityXmlLoader implements JPSecurityLoader {
  private static final Logger LOG = LoggerFactory.getLogger(JPSecurityXmlLoader.class);
  /**
   * Папка с описанием настроек доступа к пакету
   */
  public static final String RESOURCES_FOLDER = "security/";

  /**
   * Вычитывает описание настроек доступа к пакету
   *
   * @return Список описания настроек доступа к пакету
   */
  public Flux<JPSecurityPackage> load() {
    return Flux.create(x -> {
      loadTo(x);
      x.complete();
    });
  }

  private void loadTo(FluxSink<JPSecurityPackage> sink) {
    URL url = null;
    try {
      url = ResourceUtils.getURL("classpath:" + JPSecurityXmlLoader.RESOURCES_FOLDER);
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
          XmlJpSecurity xmlJpSecurity = new XmlMapper().readValue(Files.newBufferedReader(entry), XmlJpSecurity.class);
          if (xmlJpSecurity == null || xmlJpSecurity.getJpPackages() == null) {
            continue;
          }
          XmlJpPackages packages = xmlJpSecurity.getJpPackages();
          if (packages.getJpPackages() == null) {
            continue;
          }
          for (XmlJpPackage pkg : packages.getJpPackages()) {
            JPSecurityPackageBean.Builder builder = JPSecurityPackageBean.newBuilder()
                .code(pkg.getCode())
                .description(pkg.getDescription())
                .qName(pkg.getqName());

            XmlJpPermitAccess permitAccess = pkg.getJpPermitAccess();
            XmlJpProhibitionAccess prohibitionAccess = pkg.getJpProhibitionAccess();
            if (permitAccess != null && permitAccess.getJpAccess() != null) {
              for (XmlJpAccess access : permitAccess.getJpAccess()) {
                builder.permitAccess(JPAccess.newBuilder()
                    .role(access.getRole())
                    .create(access.isCreate())
                    .read(access.isRead())
                    .delete(access.isDelete())
                    .update(access.isUpdate())
                    .build());
              }
            }
            if (prohibitionAccess != null && prohibitionAccess.getJpAccess() != null) {
              for (XmlJpAccess access : prohibitionAccess.getJpAccess()) {
                builder.prohibitionAccess(JPAccess.newBuilder()
                    .role(access.getRole())
                    .create(access.isCreate())
                    .read(access.isRead())
                    .delete(access.isDelete())
                    .update(access.isUpdate())
                    .build());
              }
            }
            sink.next(JPImmutableSecurityPackageBean.newBuilder().secPackage(builder.build()).build());
          }
        }
      }
    } catch (IOException e) {
      throw JPRuntimeException.wrapException(e);
    }
  }
}