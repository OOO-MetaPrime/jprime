package mp.jprime.xml;

import mp.jprime.xml.exceptions.JPXsdValidationException;
import mp.jprime.xml.factories.JPSchemaFactory;
import mp.jprime.xml.utils.ValidationUtils;
import mp.jprime.xml.validation.ValidationEventCollector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.io.InputStream;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ValidationUtilsTest.Config.class)
public class ValidationUtilsTest {

  @Configuration
  public static class Config {
  }

  @Value("classpath:xml/validation/test-validation-success.xsd")
  private Resource xsd_success;

  @Value("classpath:xml/validation/test-validation-success.zip")
  private Resource zip_success;

  @Value("classpath:xml/validation/test-validation-unsafe-location.xsd")
  private Resource xsd_unsafe_location;

  @Value("classpath:xml/validation/test-validation-unsafe-location.zip")
  private Resource zip_unsafe_location;

  @Value("classpath:xml/validation/test-validation-unsafe-doctype.xsd")
  private Resource xsd_unsafe_doctype;

  @Value("classpath:xml/validation/test-validation-unsafe-doctype.zip")
  private Resource zip_unsafe_doctype;

  @Value("classpath:xml/validation/test-validation.xml")
  private Resource xml;


  @Test
  void testValidate_success() {
    Assertions.assertDoesNotThrow(
        () -> {
          try (InputStream is = xml.getInputStream()) {
            ValidationUtils.validate(xsd_success, is);
          }
        },
        "Validation failed"
    );
  }


  @Test
  void testValidate_unsafeImportLocation() {
    Assertions.assertThrows(
        JPXsdValidationException.class,
        () -> {
          try (InputStream is = xml.getInputStream()) {
            ValidationUtils.validate(xsd_unsafe_location, is);
          }
        }
    );
  }


  @Test
  void testValidate_unsafeDocType() {
    Assertions.assertThrows(
        JPXsdValidationException.class,
        () -> {
          try (InputStream is = xml.getInputStream()) {
            ValidationUtils.validate(xsd_unsafe_doctype, is);
          }
        }
    );
  }

  @Test
  void testValidate_zip_success() throws IOException {
    ValidationEventCollector vec = new ValidationEventCollector();
    try (InputStream is = xml.getInputStream()) {
      ValidationUtils.validate(
          is,
          JPSchemaFactory.getSchema(JPSchemaFactory.getSchemaSources(zip_success.getInputStream())),
          vec
      );
    }
    Assertions.assertFalse(vec.hasError());
  }

  @Test
  void testValidate_zip_unsafeImportLocation() {
    ValidationEventCollector vec = new ValidationEventCollector();
    Assertions.assertThrows(
        JPXsdValidationException.class,
        () -> {
          try (InputStream is = xml.getInputStream()) {
            ValidationUtils.validate(
                is,
                JPSchemaFactory.getSchema(JPSchemaFactory.getSchemaSources(zip_unsafe_location.getInputStream())),
                vec
            );
          }
        }
    );
  }

  @Test
  void testValidate_zip_unsafeDocType() {
    ValidationEventCollector vec = new ValidationEventCollector();
    Assertions.assertThrows(
        JPXsdValidationException.class,
        () -> {
          try (InputStream is = xml.getInputStream()) {
            ValidationUtils.validate(
                is,
                JPSchemaFactory.getSchema(JPSchemaFactory.getSchemaSources(zip_unsafe_doctype.getInputStream())),
                vec
            );
          }
        }
    );
  }
}
