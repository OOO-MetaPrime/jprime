package mp.jprime.utils.loaders.xml;

import tools.jackson.dataformat.xml.XmlMapper;
import mp.jprime.utils.loaders.xml.beans.XmlJpUtilSettings;
import mp.jprime.utils.loaders.xml.services.JPUtilXmlCommonResources;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.InputStream;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class XmlJpUtilSettingsTest {
  @Test
  void testCorrectReadFile() throws Exception {
    URL url = ResourceUtils.getURL("classpath:" + JPUtilXmlCommonResources.RESOURCES_FOLDER + "jpUtilSettings.xml");

    XmlJpUtilSettings xml;
    try (InputStream inputStream = url.openStream()) {
      xml = new XmlMapper().readValue(inputStream, XmlJpUtilSettings.class);
    }
    assertNotNull(xml);
  }
}
