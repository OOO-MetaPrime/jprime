package mp.jprime.actuator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Публикует информацию о сборке из {@code MANIFEST.MF}
 */
@Component
public class JPBuildInfoContributor implements InfoContributor {
  private static final Logger LOG = LoggerFactory.getLogger(JPBuildInfoContributor.class);

  private static final String PROJECT_VERSION = "Project-Version";
  private static final Attributes.Name PROJECT_VERSION_ATTR = new Attributes.Name(PROJECT_VERSION);
  private static final String BUILD_DATE = "Build-Date";
  private static final Attributes.Name BUILD_DATE_ATTR = new Attributes.Name(BUILD_DATE);
  private static final String ROOT_BUILD_TITLE = "Информация о сборке:";
  private static final String BUILD_DATE_TITLE = "Дата сборки";
  private static final String SUBMODULE_VERSION_TITLE = "Версия %s";
  private static final String PROJECT_VERSION_TITLE = "Проектная версия";
  private static final String UNKNOWN_VALUE = "—";
  private static final String SUBMODULES_SECTION_NAME = "submodules";

  private Manifest manifest;

  @Value("classpath:META-INF/MANIFEST.MF")
  private void setManifest(Resource resource) {
    if (!resource.exists()) {
      LOG.warn("MANIFEST.MF not found");
      return;
    }
    try (final InputStream stream = resource.getInputStream()) {
      this.manifest = new Manifest(stream);
    } catch (IOException e) {
      LOG.error("Error during reading MANIFEST.MF: {}", e.getMessage(), e);
    }
  }

  @Override
  public void contribute(Info.Builder builder) {
    Map<String, String> details = new LinkedHashMap<>();
    Attributes mainAttributes = manifest != null ? manifest.getMainAttributes() : new Attributes();
    Attributes submodules = manifest != null ? manifest.getAttributes(SUBMODULES_SECTION_NAME) : new Attributes();

    String projectVersion = mainAttributes.containsKey(PROJECT_VERSION_ATTR) ? mainAttributes.getValue(PROJECT_VERSION_ATTR) : UNKNOWN_VALUE;
    String buildDate = mainAttributes.containsKey(BUILD_DATE_ATTR) ? mainAttributes.getValue(BUILD_DATE_ATTR) : UNKNOWN_VALUE;

    details.put(BUILD_DATE_TITLE, buildDate);
    details.put(PROJECT_VERSION_TITLE, projectVersion);

    if (submodules != null) {
      Map<String, String> sortedSubmodules = new TreeMap<>();
      for (Map.Entry<Object, Object> entry : submodules.entrySet()) {
        sortedSubmodules.put(
            String.format(SUBMODULE_VERSION_TITLE, entry.getKey().toString()),
            entry.getValue().toString()
        );
      }
      details.putAll(sortedSubmodules);
    }

    builder.withDetail(ROOT_BUILD_TITLE, details);
  }
}
