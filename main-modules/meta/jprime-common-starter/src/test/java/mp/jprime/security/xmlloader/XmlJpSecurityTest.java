package mp.jprime.security.xmlloader;


import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import mp.jprime.security.xmlloader.beans.XmlJpSecurity;
import mp.jprime.security.xmlloader.services.JPSecurityXmlLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
class XmlJpSecurityTest {
  @Test
  void testCorrectReadFile() throws Exception {
    URL url = ResourceUtils.getURL("classpath:" + JPSecurityXmlLoader.RESOURCES_FOLDER + "jpPackages.xml");

    XmlJpSecurity xmlJpSecurity = new XmlMapper().readValue(url, XmlJpSecurity.class);
    assertNotNull(xmlJpSecurity);
  }
}
