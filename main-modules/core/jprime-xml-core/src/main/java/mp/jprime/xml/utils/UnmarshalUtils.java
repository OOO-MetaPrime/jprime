package mp.jprime.xml.utils;

import mp.jprime.xml.exceptions.JPXmlUnmarshalException;
import mp.jprime.xml.factories.JPSchemaFactory;
import mp.jprime.xml.XMLOutputOptions;
import org.glassfish.jaxb.runtime.v2.runtime.unmarshaller.SAXConnector;
import org.springframework.core.io.Resource;
import org.w3c.dom.Node;
import org.xml.sax.*;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.XMLConstants;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.ValidationEventHandler;

import javax.xml.namespace.QName;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

/**
 * Набор утилитарных методов преобразования из XML-данных
 */
public final class UnmarshalUtils {
  private UnmarshalUtils() { }

  /**
   * Чтение из xml-элемента
   *
   * @param node             xml-элемент
   * @param tClass           тип сущности
   * @param <T>              класс результата
   * @param classesToBeBound классы контекста преобразования
   * @return объект класса
   */
  public static <T> T unmarshal(Node node, Class<T> tClass, Class... classesToBeBound) {
    try {
      Unmarshaller jaxbUnmarshaller = getUnmarshaller(tClass, null, null, classesToBeBound);
      Object o = jaxbUnmarshaller.unmarshal(node);
      if (o instanceof JAXBElement) {
        o = ((JAXBElement) o).getValue();
      }
      return (T) o;
    } catch (Exception e) {
      throw new JPXmlUnmarshalException(String.format("Ошибка преобразования xml-элемент в объект класса: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Чтение из xml-элемента
   *
   * @param node             xml-элемент
   * @param <T>              класс результата
   * @return объект класса
   */
  public static <T> T unmarshalNode(Node node, Class<T> clazz) {
    try {
      Unmarshaller unmarshaller = getUnmarshaller(clazz, null, null, null);
      return unmarshaller.unmarshal(new DOMSource(node), clazz).getValue();
    } catch (Exception e) {
      throw new RuntimeException(String.format("Ошибка преобразования node-элемента в объект класса: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Чтение из xml-элемента
   *
   * @param xmlString        xml-строка
   * @param tClass           тип сущности
   * @param <T>              класс результата
   * @param classesToBeBound классы контекста преобразования
   * @return объект класса
   */
  public static <T> T unmarshal(String xmlString, Class<T> tClass, Class... classesToBeBound) {
    try (ByteArrayInputStream is = new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8))) {
      Unmarshaller jaxbUnmarshaller = getUnmarshaller(tClass, null, null, classesToBeBound);
      Object o = jaxbUnmarshaller.unmarshal(is);
      if (o instanceof JAXBElement) {
        o = ((JAXBElement) o).getValue();
      }
      return (T) o;
    } catch (Exception e) {
      throw new JPXmlUnmarshalException(String.format("Ошибка преобразования xml-элемент в объект класса: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Преобразования xml-элемента в массив байтов
   *
   * @param node xml-элемент
   * @return массив байтов
   */
  public static byte[] toByteArray(Node node) {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
      TransformerFactory transFactory = TransformerFactory.newInstance();
      Transformer transformer = transFactory.newTransformer();
      transformer.transform(new DOMSource(node), new StreamResult(bos));
      return bos.toByteArray();
    } catch (Exception e) {
      throw new JPXmlUnmarshalException(String.format("Ошибка преобразования xml-элемента в массив байтов: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Преобразования xml-элемента в строку
   *
   * @param node xml-элемент
   * @return строка
   */
  public static String toString(Node node) {
    return toString(node, null);
  }

  /**
   * Преобразования xml-элемента в строку
   *
   * @param node    xml-элемент
   * @param options опции
   * @return строка
   */
  public static String toString(Node node, XMLOutputOptions options) {
    try {
      TransformerFactory transFactory = TransformerFactory.newInstance();
      Transformer transformer = transFactory.newTransformer();
      if (options != null) {
        if (options.isNoProcessingInstruction()) {
          transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        }
        if (options.isIndent()) {
          transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        }
        if (options.getEncoding() != null && !options.getEncoding().isEmpty()) {
          transformer.setOutputProperty(OutputKeys.ENCODING, options.getEncoding());
        }
      }
      StringWriter buffer = new StringWriter();
      transformer.transform(new DOMSource(node),
          new StreamResult(buffer));
      return buffer.toString();
    } catch (TransformerException e) {
      throw new JPXmlUnmarshalException(String.format("Ошибка преобразования xml-элемента в строку: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Чтение из xml-элемента
   *
   * @param stream           поток байтов
   * @param tClass           тип сущности
   * @param <T>              класс результата
   * @param classesToBeBound классы контекста преобразования
   * @return объект класса
   */
  public static <T> T unmarshal(InputStream stream, Class<T> tClass, Class... classesToBeBound) {
    try {
      Unmarshaller jaxbUnmarshaller = getUnmarshaller(tClass, null, null, classesToBeBound);
      Object o = jaxbUnmarshaller.unmarshal(stream);
      if (o instanceof JAXBElement) {
        o = ((JAXBElement) o).getValue();
      }
      return (T) o;
    } catch (Exception e) {
      throw new JPXmlUnmarshalException(String.format("Ошибка преобразования xml-элемент в объект класса: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Чтение из xml-элемента
   *
   * @param node             xml-элемент
   * @param tClass           тип сущности
   * @param <T>              класс результата
   * @param schema           схема данных
   * @param eventHandler     обработчик событий валидации
   * @param classesToBeBound классы контекста преобразования
   * @return объект класса
   */
  public static <T> T unmarshal(Node node, Class<T> tClass, Resource schema, ValidationEventHandler eventHandler, Class... classesToBeBound) {
    if (schema == null) {
      throw new JPXmlUnmarshalException("Не удалось получить XSD-схему для валидации");
    }
    try {
      Unmarshaller jaxbUnmarshaller = getUnmarshaller(tClass, schema, eventHandler, classesToBeBound);
      Object o = jaxbUnmarshaller.unmarshal(node);
      if (o instanceof JAXBElement) {
        o = ((JAXBElement) o).getValue();
      }
      return (T) o;
    } catch (Exception e) {
      throw new JPXmlUnmarshalException(String.format("Ошибка преобразования xml-элемент в объект класса: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Чтение из xml-элемента
   *
   * @param stream           поток байтов
   * @param tClass           тип сущности
   * @param <T>              класс результата
   * @param schema           схема данных
   * @param eventHandler     обработчик событий валидации
   * @param classesToBeBound классы контекста преобразования
   * @return объект класса
   */
  public static <T> T unmarshal(InputStream stream, Class<T> tClass, Resource schema, ValidationEventHandler eventHandler, Class... classesToBeBound) {
    if (schema == null) {
      throw new JPXmlUnmarshalException("Не удалось получить XSD-схему для валидации");
    }
    try {
      Unmarshaller jaxbUnmarshaller = getUnmarshaller(tClass, schema, eventHandler, classesToBeBound);
      Object o = jaxbUnmarshaller.unmarshal(stream);
      if (o instanceof JAXBElement) {
        o = ((JAXBElement) o).getValue();
      }
      return (T) o;
    } catch (Exception e) {
      throw new JPXmlUnmarshalException(String.format("Ошибка преобразования xml-элемент в объект класса: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Чтение из xml-элемента
   *
   * @param xmlString        xml-строка
   * @param tClass           тип сущности
   * @param <T>              класс результата
   * @param schema           схема данных
   * @param eventHandler     обработчик событий валидации
   * @param classesToBeBound классы контекста преобразования
   * @return объект класса
   */
  public static <T> T unmarshal(String xmlString, Class<T> tClass, Resource schema, ValidationEventHandler eventHandler, Class... classesToBeBound) {
    if (schema == null) {
      throw new JPXmlUnmarshalException("Не удалось получить XSD-схему для валидации");
    }
    try (ByteArrayInputStream is = new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8))) {
      return unmarshal(is, tClass, schema, eventHandler, classesToBeBound);
    } catch (Exception e) {
      throw new JPXmlUnmarshalException(String.format("Ошибка преобразования xml-элемент в объект класса: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Чтение из xml-элемента
   *
   * @param source           источник данных
   * @param tClass           тип сущности
   * @param <T>              класс результата
   * @param schema           схема данных
   * @param eventHandler     обработчик событий валидации
   * @param classesToBeBound классы контекста преобразования
   * @return объект класса
   */
  public static <T> T unmarshal(Source source, Class<T> tClass, Resource schema, ValidationEventHandler eventHandler, Class... classesToBeBound) {
    if (schema == null) {
      throw new JPXmlUnmarshalException("Не удалось получить XSD-схему для валидации");
    }
    try {
      Unmarshaller jaxbUnmarshaller = getUnmarshaller(tClass, schema, eventHandler, classesToBeBound);
      Object o = jaxbUnmarshaller.unmarshal(source);
      if (o instanceof JAXBElement) {
        o = ((JAXBElement) o).getValue();
      }
      return (T) o;
    } catch (Exception e) {
      throw new JPXmlUnmarshalException(String.format("Ошибка преобразования xml-элемент в объект класса: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Чтение из xml-элемента
   *
   * @param source           источник данных
   * @param tClass           тип сущности
   * @param <T>              класс результата
   * @param classesToBeBound классы контекста преобразования
   * @return объект класса
   */
  public static <T> T unmarshal(Source source, Class<T> tClass, Class... classesToBeBound) {
    try {
      Unmarshaller jaxbUnmarshaller = getUnmarshaller(tClass, null, null, classesToBeBound);
      Object o = jaxbUnmarshaller.unmarshal(source);
      if (o instanceof JAXBElement) {
        o = ((JAXBElement) o).getValue();
      }
      return (T) o;
    } catch (Exception e) {
      throw new JPXmlUnmarshalException(String.format("Ошибка преобразования xml-элемент в объект класса: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Чтение из xml-элемента с отсутствующими namespace
   *
   * @param bytes            массив байтов
   * @param tClass           тип сущности
   * @param classesToBeBound классы контекста преобразования
   * @param <T>              класс результата
   * @return объект класса
   */
  public static <T> T unmarshalUnqualifiedNamespace(byte[] bytes, Class<T> tClass, Class... classesToBeBound) {
    try (ByteArrayInputStream is = new ByteArrayInputStream(bytes)) {
      return unmarshalUnqualifiedNamespace(is, tClass, classesToBeBound);
    } catch (IOException e) {
      throw new JPXmlUnmarshalException(String.format("Ошибка преобразования xml-элемент в объект класса: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Чтение из xml-элемента с отсутствующими namespace
   *
   * @param stream           поток байтов
   * @param tClass           тип сущности
   * @param classesToBeBound классы контекста преобразования
   * @param <T>              класс результата
   * @return объект класса
   */
  public static <T> T unmarshalUnqualifiedNamespace(InputStream stream, Class<T> tClass, Class... classesToBeBound) {
    try {
      XMLReader reader = XMLReaderFactory.createXMLReader();

      reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
      reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
      reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
      reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

      try {
        reader.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        reader.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        reader.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
      } catch (Exception ignored) {
        // Свойства могут быть недоступны для некоторых SAX-фабрик — это нормально
      }

      UnqualifiedNamespaceFilter filter = new UnqualifiedNamespaceFilter();
      filter.setParent(reader);

      return unmarshal(new SAXSource(filter, new InputSource(stream)), tClass, classesToBeBound);
    } catch (Exception e) {
      throw new JPXmlUnmarshalException(String.format("Ошибка преобразования xml-элемент в объект класса: \n%s", e.getMessage()), e);
    }
  }

  private static Unmarshaller getUnmarshaller(Class dataClass, Resource scheme, ValidationEventHandler eventHandler, Class... classesToBeBound) throws Exception {
    JAXBContext jaxbContext = classesToBeBound != null && classesToBeBound.length > 0 ? JAXBContext.newInstance(classesToBeBound) : JAXBContext.newInstance(dataClass);
    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

    if (scheme != null) {
      jaxbUnmarshaller.setSchema(JPSchemaFactory.getSchema(scheme));
      if (eventHandler != null) {
        jaxbUnmarshaller.setEventHandler(eventHandler);
      }
    }
    return jaxbUnmarshaller;
  }

  /**
   * Фильтр XML
   * Игноририрует отсутствие немспейсов и парсит XMLку по localName
   */
  private final static class UnqualifiedNamespaceFilter extends XMLFilterImpl {
    private SAXConnector saxConnector;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
      if (saxConnector != null) {
        Collection<QName> expected = saxConnector.getContext().getCurrentExpectedElements();
        for (QName expectedQname : expected) {
          if (localName.equals(expectedQname.getLocalPart())) {
            super.startElement(expectedQname.getNamespaceURI(), localName, qName, atts);
            return;
          }
        }
      }
      super.startElement(uri, localName, qName, atts);
    }

    @Override
    public void setContentHandler(ContentHandler handler) {
      super.setContentHandler(handler);
      if (handler instanceof SAXConnector) {
        saxConnector = (SAXConnector) handler;
      }
    }

  }
}
