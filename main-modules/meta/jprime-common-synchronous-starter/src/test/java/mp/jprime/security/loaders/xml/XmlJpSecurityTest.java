package mp.jprime.security.loaders.xml;


import tools.jackson.dataformat.xml.XmlMapper;
import mp.jprime.security.loaders.xml.beans.XmlJpSecurity;
import mp.jprime.security.loaders.xml.services.JPSecurityXmlLoader;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.InputStream;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class XmlJpSecurityTest {
  @Test
  void testCorrectReadFile() throws Exception {
    URL url = ResourceUtils.getURL("classpath:" + JPSecurityXmlLoader.RESOURCES_FOLDER + "jpPackages.xml");

    try (InputStream inputStream = url.openStream()) {
      XmlJpSecurity xmlJpSecurity = new XmlMapper().readValue(inputStream, XmlJpSecurity.class);
      assertNotNull(xmlJpSecurity);
    }
  }
}
