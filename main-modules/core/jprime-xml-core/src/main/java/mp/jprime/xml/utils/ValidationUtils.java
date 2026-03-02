package mp.jprime.xml.utils;

import jakarta.xml.bind.*;
import mp.jprime.xml.factories.JPSchemaFactory;
import mp.jprime.xml.exceptions.JPXsdValidationException;
import mp.jprime.xml.validation.ErrorHandlerToValidationEventHandlerAdapter;
import org.springframework.core.io.Resource;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

/**
 * Проверяет xsd-схемой
 */
public final class ValidationUtils {
  private ValidationUtils() {
  }

  private static final org.xml.sax.ContentHandler noopContentHandler = new DefaultHandler();

  /**
   * Проверяет XML XSD-схемой
   *
   * @param xml          xml
   * @param schema       xsd-схема
   * @param eventHandler обработчик ошибок
   */
  public static void validate(byte[] xml, Schema schema, ValidationEventHandler eventHandler) {
    try (InputStream is = new ByteArrayInputStream(xml)) {
      validate(is, schema, eventHandler);
    } catch (Exception e) {
      throw new JPXsdValidationException("Failed to validate XML", e);
    }
  }

  /**
   * Проверяет XML XSD-схемой
   *
   * @param xmlStream    xml
   * @param schema       xsd-схема
   * @param eventHandler обработчик ошибок
   */
  public static void validate(InputStream xmlStream, Schema schema, ValidationEventHandler eventHandler) {
    try {
      Source requestXmlSource = new StreamSource(xmlStream);
      Validator validator = schema.newValidator();
      validator.setErrorHandler(ErrorHandlerToValidationEventHandlerAdapter.of(eventHandler));
      validator.validate(requestXmlSource);
    } catch (Exception e) {
      throw new JPXsdValidationException("Failed to validate XML", e);
    }
  }

  /**
   * Проверяет XML XSD-схемой
   *
   * @param schemaResource XSD-схема
   * @param xmlStream      XML
   */
  public static void validate(Resource schemaResource, InputStream xmlStream) {
    if (schemaResource == null) {
      throw new JPXsdValidationException("Не удалось получить XSD-схему для валидации");
    }
    try {
      Schema schema = JPSchemaFactory.getSchema(schemaResource);
      Validator validator = schema.newValidator();

      XMLReader xmlReader = XMLReaderFactory.createXMLReader();

      xmlReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
      xmlReader.setFeature("http://xml.org/sax/features/external-general-entities", false);
      xmlReader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
      xmlReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

      try {
        xmlReader.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        xmlReader.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        xmlReader.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "file");
      } catch (Exception ignored) {
        // Свойства могут быть недоступны для некоторых SAX-фабрик — это нормально
      }

      // Оборачиваем xmlReader в SAXSource для валидатора
      InputSource inputSource = new InputSource(xmlStream);
      SAXSource saxSource = new SAXSource(xmlReader, inputSource);
      validator.validate(saxSource);
    } catch (Exception e) {
      throw new JPXsdValidationException("Failed to validate XML [schema=" + schemaResource + "]", e);
    }
  }

  /**
   * Преобразует объект класса в массив байтов
   *
   * @param data             объект класса
   * @param qName            квалификационное имя результирующего xml-элемента
   * @param schema           схема данных
   * @param eventHandler     обработчик событий валидации
   * @param classesToBeBound классы контекста преобразования
   * @return массив байтов
   */
  public static void validate(Object data, QName qName, Resource schema, ValidationEventHandler eventHandler, Class<?>... classesToBeBound) {
    if (schema == null) {
      throw new JPXsdValidationException("Не удалось получить XSD-схему для валидации");
    }
    try {
      Marshaller jaxbMarshaller = getMarshaller(data.getClass(), schema, eventHandler, classesToBeBound);
      jaxbMarshaller.marshal(getJAXBElement(qName, data), noopContentHandler);
    } catch (Exception e) {
      throw new JPXsdValidationException(String.format("Ошибка проверки класса схемой: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Возвращает отформатированную строку соообщений с ошибками валидации xsd
   *
   * @param message сообщение об ошибке
   * @param errors  контейнер ошибок валидации
   * @return отформатированная строка
   */
  public static String getXmlValidationErrorMessage(String message, Map<Integer, Collection<String>> errors) {
    StringBuilder err = new StringBuilder("\nОшибка отправки запроса: \n").append(message);
    if (errors != null && !errors.isEmpty()) {
      // отображаем ошибки в порядке убывания критичности
      errors.keySet().stream()
          .sorted(Comparator.reverseOrder())
          .forEach((severity) -> {
            String str = "";
            switch (severity) {
              case ValidationEvent.WARNING:
                str = "Предупреждение";
                break;
              case ValidationEvent.ERROR:
                str = "Ошибка";
                break;
              case ValidationEvent.FATAL_ERROR:
                str = "Критическая ошибка";
                break;
            }
            String finalStr = str;
            errors.get(severity).forEach(msg -> err.append(String.format("\n%s: %s", finalStr, msg)));
          });
    }
    return err.toString();
  }

  private static Marshaller getMarshaller(Class<?> dataClass, Resource scheme, ValidationEventHandler eventHandler, Class<?>... classesToBeBound) throws Exception {
    JAXBContext jaxbContext = classesToBeBound != null && classesToBeBound.length > 0 ? JAXBContext.newInstance(classesToBeBound) : JAXBContext.newInstance(dataClass);
    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

    if (scheme != null) {
      jaxbMarshaller.setSchema(JPSchemaFactory.getSchema(scheme));
      if (eventHandler != null) {
        jaxbMarshaller.setEventHandler(eventHandler);
      }
    }
    return jaxbMarshaller;
  }

  @SuppressWarnings("unchecked")
  private static <T> JAXBElement<T> getJAXBElement(QName qName, T data) {
    return new JAXBElement<>(qName, (Class<T>) data.getClass(), data);
  }

  private static void setupSecurity(XMLReader xmlReader) {
    try {
      xmlReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    } catch (Exception ignored) {
      // Свойства могут быть недоступны для некоторых SAX-фабрик — это нормально
    }
    try {
      xmlReader.setFeature("http://xml.org/sax/features/external-general-entities", false);
    } catch (Exception ignored) {
      // Свойства могут быть недоступны для некоторых SAX-фабрик — это нормально
    }
    try {
      xmlReader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
    } catch (Exception ignored) {
      // Свойства могут быть недоступны для некоторых SAX-фабрик — это нормально
    }
    try {
      xmlReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
    } catch (Exception ignored) {
      // Свойства могут быть недоступны для некоторых SAX-фабрик — это нормально
    }

    try {
      xmlReader.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    } catch (Exception ignored) {
      // Свойства могут быть недоступны для некоторых SAX-фабрик — это нормально
    }
    try {
      xmlReader.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    } catch (Exception ignored) {
      // Свойства могут быть недоступны для некоторых SAX-фабрик — это нормально
    }
    try {
      // без file не подгружаются импортированные схемы, file - имя протокола, разрешаем локальные схемы
      xmlReader.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "file");
    } catch (Exception ignored) {
      // Свойства могут быть недоступны для некоторых SAX-фабрик — это нормально
    }
  }
}
