package mp.jprime.metamaps.loaders.xml;


import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import mp.jprime.metamaps.loaders.xml.beans.XmlJpClassMaps;
import mp.jprime.metamaps.loaders.xml.services.JPMapsXmlCommonResources;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class XmlJpClassMapsTest {
  @Test
  void testCorrectReadFile() throws Exception {
    URL url = ResourceUtils.getURL("classpath:" + JPMapsXmlCommonResources.RESOURCES_FOLDER + "jpClassMaps.xml");

    XmlJpClassMaps xmlJpClassMaps = new XmlMapper().readValue(url, XmlJpClassMaps.class);
    assertNotNull(xmlJpClassMaps);
  }
}
