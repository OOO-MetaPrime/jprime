package mp.jprime.schedule.loaders.xml;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import mp.jprime.schedule.loaders.xml.beans.XmlJpTaskSettings;
import mp.jprime.schedule.loaders.xml.services.JpScheduleTaskXmlCommonResources;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class XmlJpScheduleTaskSettingsTest {
  @Test
  void testCorrectReadFile() throws Exception {
    URL url = ResourceUtils.getURL("classpath:" + JpScheduleTaskXmlCommonResources.RESOURCES_FOLDER + "jpTaskSettings.xml");

    XmlJpTaskSettings xml = new XmlMapper().readValue(url, XmlJpTaskSettings.class);
    assertNotNull(xml);
  }
}
