package mp.jprime.security.abac.loaders.xml;

import tools.jackson.dataformat.xml.XmlMapper;
import mp.jprime.security.abac.loaders.xml.beans.XmlJpAbac;
import mp.jprime.security.abac.loaders.xml.services.JPAbacXmlLoader;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.InputStream;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class XmlAbacTest {
  @Test
  void testCorrectReadFile() throws Exception {
    URL url = ResourceUtils.getURL("classpath:" + JPAbacXmlLoader.RESOURCES_FOLDER + "policies.xml");

    try (InputStream inputStream = url.openStream()) {
      XmlJpAbac xmlJpAbac = new XmlMapper().readValue(inputStream, XmlJpAbac.class);
      assertNotNull(xmlJpAbac);
    }
  }
}
