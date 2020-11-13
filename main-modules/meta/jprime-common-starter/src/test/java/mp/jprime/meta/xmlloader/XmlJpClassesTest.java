package mp.jprime.meta.xmlloader;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import mp.jprime.meta.xmlloader.services.JPMetaXmlLoader;
import mp.jprime.meta.xmlloader.beans.XmlJpClasses;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.net.URL;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
public class XmlJpClassesTest {
  @Test
  public void testCorrectReadFile() throws Exception {
    URL url = ResourceUtils.getURL("classpath:" + JPMetaXmlLoader.RESOURCES_FOLDER + "jpClass-xml.xml");

    XmlJpClasses xmlJpClasses = new XmlMapper().readValue(url, XmlJpClasses.class);
    assertNotNull(xmlJpClasses);
  }
}
