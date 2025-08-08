package mp.jprime.xml.services;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.xml.modules.JPObjectMapperXmlExpander;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLStreamWriter;
import java.util.Collection;

/**
 * Базовый класс XML-обработчиков
 */
@Service
public class JPXmlMapper extends JPObjectXmlMapper {
  private final XmlMapper XML_MAPPER;

  private JPXmlMapper(@Autowired Collection<JPObjectMapperXmlExpander> expanders) {
    XML_MAPPER = new XmlMapper();
    setSettings(expanders, XML_MAPPER);
  }

  @Override
  public XmlMapper getObjectMapper() {
    return XML_MAPPER;
  }

  public <T> T toObject(Class<T> to, byte[] src) {
    if (src == null) {
      return null;
    }
    if (to == null) {
      throw new IllegalArgumentException("Unset destination type <to> on call JPXmlMapper");
    }
    try {
      return XML_MAPPER.readValue(src, to);
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  public void writeValue(XMLStreamWriter stream, Object value) {
    if (value == null) {
      return;
    }
    if (stream == null) {
      throw new IllegalArgumentException("Unset destination stream on call JPXmlMapper");
    }
    try {
      XML_MAPPER.writeValue(stream, value);
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

}
