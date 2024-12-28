package mp.jprime.meta.xmlloader;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import mp.jprime.meta.xmlloader.beans.XmlJpAttr;
import mp.jprime.meta.xmlloader.beans.XmlJpClass;
import mp.jprime.meta.xmlloader.beans.XmlJpClasses;
import mp.jprime.meta.xmlloader.services.JPMetaXmlBaseResources;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class XmlJpClassesTest {
  @Test
  void testCorrectReadFile() throws Exception {
    URL url = ResourceUtils.getURL("classpath:" + JPMetaXmlBaseResources.RESOURCES_FOLDER + "jpClass-xml.xml");

    XmlJpClasses xmlJpClasses = new XmlMapper().readValue(url, XmlJpClasses.class);
    assertNotNull(xmlJpClasses);

    XmlJpClass cls = xmlJpClasses.getJpClasses()[0];
    assertNotNull(cls);

    assertEquals("personalCard", cls.getCode());

    for (XmlJpAttr attr : cls.getJpAttrs().getJpAttrs()) {
      if ("moneyAttr".equals(attr.getCode())) {
        assertEquals("RUR", attr.getMoney().getCurrencyCode());
      } else if ("geometryAttr".equals(attr.getCode())) {
        assertEquals(3857, attr.getGeometry().getSRID());
      }
    }
  }
}
