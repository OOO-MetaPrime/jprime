package mp.jprime.meta.xmlloader;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import mp.jprime.meta.xmlloader.beans.XmlJpClasses;
import mp.jprime.meta.xmlloader.services.JPMetaXmlLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
class XmlJpClassesTest {
  @Test
  void testCorrectReadFile() throws Exception {
    URL url = ResourceUtils.getURL("classpath:" + JPMetaXmlLoader.RESOURCES_FOLDER + "jpClass-xml.xml");

    XmlJpClasses xmlJpClasses = new XmlMapper().readValue(url, XmlJpClasses.class);
    assertNotNull(xmlJpClasses);
  }
}
