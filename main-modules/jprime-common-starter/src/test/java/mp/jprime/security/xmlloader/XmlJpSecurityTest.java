package mp.jprime.security.xmlloader;


import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import mp.jprime.security.xmlloader.beans.XmlJpSecurity;
import mp.jprime.security.xmlloader.services.JPSecurityXmlLoader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.net.URL;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
public class XmlJpSecurityTest {
  @Test
  public void testCorrectReadFile() throws Exception {
    URL url = ResourceUtils.getURL("classpath:" + JPSecurityXmlLoader.RESOURCES_FOLDER + "jpPackages.xml");

    XmlJpSecurity xmlJpSecurity = new XmlMapper().readValue(url, XmlJpSecurity.class);
    assertNotNull(xmlJpSecurity);
  }
}
