package mp.jprime.metamaps.xmlloader;


import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import mp.jprime.metamaps.xmlloader.services.JPMapsXmlLoader;
import mp.jprime.metamaps.xmlloader.beans.XmlJpClassMaps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.net.URL;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
public class XmlJpClassMapsTest {
  @Test
  public void testCorrectReadFile() throws Exception {
    URL url = ResourceUtils.getURL("classpath:" + JPMapsXmlLoader.RESOURCES_FOLDER + "jpClassMaps.xml");

    XmlJpClassMaps xmlJpClassMaps = new XmlMapper().readValue(url, XmlJpClassMaps.class);
    assertNotNull(xmlJpClassMaps);
  }
}
