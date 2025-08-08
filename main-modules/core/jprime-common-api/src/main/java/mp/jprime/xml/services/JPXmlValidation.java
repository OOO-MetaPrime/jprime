package mp.jprime.xml.services;

import org.xml.sax.*;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Валидация XML XSD-схемой
 */
public abstract class JPXmlValidation {

  /**
   * Проверяет XML XSD-схемой
   *
   * @param scheme XSD-схема
   * @param xmlStream XML
   */
  public static void validate(URL scheme, InputStream xmlStream) {
    try {
      SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      Schema schema = factory.newSchema(scheme);
      Validator validator = schema.newValidator();
      validator.validate(new StreamSource(xmlStream));
    } catch (SAXException | IOException e) {
      throw new IllegalStateException("Failed to validate XML [scheme="+scheme+"]", e);
    }
  }
}
