package mp.jprime.security.loaders.xml.services;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.security.JPSecurityLoader;
import mp.jprime.security.JPSecurityPackage;
import mp.jprime.security.beans.JPSecurityPackageAccessBean;
import mp.jprime.security.beans.JPSecurityPackageBean;
import mp.jprime.security.loaders.xml.beans.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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

  private ApplicationContext applicationContext;

  @Autowired
  private void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Override
  public Collection<JPSecurityPackage> load() {
    try {
      Resource[] resources = null;
      try {
        resources = applicationContext.getResources("classpath:" + JPSecurityXmlLoader.RESOURCES_FOLDER + "*.xml");
      } catch (FileNotFoundException e) {
        LOG.debug(e.getMessage(), e);
      }
      if (resources == null || resources.length == 0) {
        return Collections.emptyList();
      }

      Collection<JPSecurityPackage> result = new ArrayList<>();
      for (Resource res : resources) {
        try (InputStream is = res.getInputStream()) {
          XmlJpSecurity xmlJpSecurity = new XmlMapper().readValue(is, XmlJpSecurity.class);
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
                .name(pkg.getqName())
                .description(pkg.getDescription())
                .qName(pkg.getqName());

            XmlJpPermitAccess permitAccess = pkg.getJpPermitAccess();
            XmlJpProhibitionAccess prohibitionAccess = pkg.getJpProhibitionAccess();
            if (permitAccess != null && permitAccess.getJpAccess() != null) {
              for (XmlJpAccess access : permitAccess.getJpAccess()) {
                builder.permitAccess(JPSecurityPackageAccessBean.newBuilder()
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
                builder.prohibitionAccess(JPSecurityPackageAccessBean.newBuilder()
                    .role(access.getRole())
                    .create(access.isCreate())
                    .read(access.isRead())
                    .delete(access.isDelete())
                    .update(access.isUpdate())
                    .build());
              }
            }
            result.add(builder.build());
          }
        }
      }
      return result;
    } catch (IOException e) {
      throw JPRuntimeException.wrapException(e);
    }
  }
}