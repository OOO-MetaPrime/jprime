package mp.jprime.xml;

import mp.jprime.xml.exceptions.JPXmlUnmarshalException;
import mp.jprime.xml.generated.ObjectFactory;
import mp.jprime.xml.generated.RootType;
import mp.jprime.xml.utils.UnmarshalUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.InputStream;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UnmarshalUtilsTest.Config.class)
public class UnmarshalUtilsTest {

  @Configuration
  public static class Config {
  }

  @Value("classpath:xml/unmarshal/test-unmarshal.xsd")
  private Resource xsd;

  @Value("classpath:xml/unmarshal/test-unmarshal-success.xml")
  private Resource xml_success;

  @Value("classpath:xml/unmarshal/test-unmarshal-unsafe-doctype.xml")
  private Resource xml_unsafe_doctype;

  @Test
  void testUnmarshal_success() {
    Assertions.assertDoesNotThrow(
        () -> {
          try (InputStream is = xml_success.getInputStream()) {
            UnmarshalUtils.unmarshal(is, RootType.class, xsd, null, ObjectFactory.class);
          }
        },
        "Validation failed"
    );
  }

  @Test
  void testValidate_unsafeDocType() {
    Assertions.assertThrows(
        JPXmlUnmarshalException.class,
        () -> {
          try (InputStream is = xml_unsafe_doctype.getInputStream()) {
            UnmarshalUtils.unmarshal(is, RootType.class, xsd, null, ObjectFactory.class);
          }
        }
    );
  }

  @Test
  void testUnmarshal_successUnqualified() {
    Assertions.assertDoesNotThrow(
        () -> {
          try (InputStream is = xml_success.getInputStream()) {
            UnmarshalUtils.unmarshalUnqualifiedNamespace(is, RootType.class, ObjectFactory.class);
          }
        },
        "Validation failed"
    );
  }
}
