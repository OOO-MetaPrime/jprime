package mp.jprime.meta.xmlloader;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import mp.jprime.meta.xmlloader.beans.XmlJpClasses;
import mp.jprime.meta.xmlloader.services.JPMetaXmlBaseResources;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class XmlJpClassesTest {
  @Test
  void testCorrectReadFile() throws Exception {
    URL url = ResourceUtils.getURL("classpath:" + JPMetaXmlBaseResources.RESOURCES_FOLDER + "jpClass-xml.xml");

    XmlJpClasses xmlJpClasses = new XmlMapper().readValue(url, XmlJpClasses.class);
    assertNotNull(xmlJpClasses);
  }
}
