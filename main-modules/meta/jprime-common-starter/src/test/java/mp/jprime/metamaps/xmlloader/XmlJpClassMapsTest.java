package mp.jprime.metamaps.xmlloader;


import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import mp.jprime.metamaps.xmlloader.beans.XmlJpClassMaps;
import mp.jprime.metamaps.xmlloader.services.JPMapsXmlLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
class XmlJpClassMapsTest {
  @Test
  void testCorrectReadFile() throws Exception {
    URL url = ResourceUtils.getURL("classpath:" + JPMapsXmlLoader.RESOURCES_FOLDER + "jpClassMaps.xml");

    XmlJpClassMaps xmlJpClassMaps = new XmlMapper().readValue(url, XmlJpClassMaps.class);
    assertNotNull(xmlJpClassMaps);
  }
}
