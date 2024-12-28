package mp.jprime.metamaps.xmlloader;


import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import mp.jprime.metamaps.xmlloader.beans.XmlJpClassMaps;
import mp.jprime.metamaps.xmlloader.services.JPMapsXmlBaseResources;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class XmlJpClassMapsTest {
  @Test
  void testCorrectReadFile() throws Exception {
    URL url = ResourceUtils.getURL("classpath:" + JPMapsXmlBaseResources.RESOURCES_FOLDER + "jpClassMaps.xml");

    XmlJpClassMaps xmlJpClassMaps = new XmlMapper().readValue(url, XmlJpClassMaps.class);
    assertNotNull(xmlJpClassMaps);
  }
}
