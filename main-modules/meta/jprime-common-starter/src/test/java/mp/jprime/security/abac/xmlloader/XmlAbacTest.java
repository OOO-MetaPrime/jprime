package mp.jprime.security.abac.xmlloader;


import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import mp.jprime.security.abac.xmlloader.beans.XmlJpAbac;
import mp.jprime.security.abac.xmlloader.services.JPAbacXmlLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
class XmlAbacTest {
  @Test
  void testCorrectReadFile() throws Exception {
    URL url = ResourceUtils.getURL("classpath:" + JPAbacXmlLoader.RESOURCES_FOLDER + "policies.xml");

    XmlJpAbac xmlJpAbac = new XmlMapper().readValue(url, XmlJpAbac.class);
    assertNotNull(xmlJpAbac);
  }
}
