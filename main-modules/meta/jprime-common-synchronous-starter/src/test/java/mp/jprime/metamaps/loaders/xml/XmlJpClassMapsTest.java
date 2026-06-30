package mp.jprime.metamaps.loaders.xml;


import tools.jackson.dataformat.xml.XmlMapper;
import mp.jprime.metamaps.loaders.xml.beans.XmlJpClassMaps;
import mp.jprime.metamaps.loaders.xml.services.JPMapsXmlCommonResources;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.InputStream;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class XmlJpClassMapsTest {
  @Test
  void testCorrectReadFile() throws Exception {
    URL url = ResourceUtils.getURL("classpath:" + JPMapsXmlCommonResources.RESOURCES_FOLDER + "jpClassMaps.xml");

    try (InputStream inputStream = url.openStream()) {
      XmlJpClassMaps xmlJpClassMaps = new XmlMapper().readValue(inputStream, XmlJpClassMaps.class);
      assertNotNull(xmlJpClassMaps);
    }
  }
}
