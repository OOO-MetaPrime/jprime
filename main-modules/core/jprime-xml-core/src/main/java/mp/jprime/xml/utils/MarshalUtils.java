package mp.jprime.xml.utils;

import mp.jprime.xml.exceptions.JPXmlMarshalException;
import mp.jprime.xml.factories.JPDocumentBuilderFactory;
import mp.jprime.xml.factories.JPSchemaFactory;
import org.glassfish.jaxb.runtime.marshaller.NamespacePrefixMapper;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.ValidationEventHandler;
import javax.xml.namespace.QName;
import java.io.*;
import java.util.Map;

/**
 * Набор утилитарных методов преобразования в XML-данные
 */
public final class MarshalUtils {
  private MarshalUtils() { }

  /**
   * Преобразует объект класса в xml-строку
   *
   * @param data             объект класса
   * @param encoding         кодировка
   * @param classesToBeBound классы контекста преобразования
   * @return xml-элемент
   */
  @SuppressWarnings("rawtypes")
  public static String toString(Object data, String encoding, Class... classesToBeBound) {
    try {
      Marshaller jaxbMarshaller = getMarshaller(data.getClass(), encoding, null, null, null, classesToBeBound);
      StringWriter sw = new StringWriter();
      jaxbMarshaller.marshal(data, sw);
      return sw.toString();
    } catch (Exception e) {
      throw new JPXmlMarshalException(String.format("Ошибка преобразования класса в xml-строку: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Преобразует объект класса в xml-строку
   *
   * @param data             объект класса
   * @param qName            квалификационное имя результирующего xml-элемента
   * @param encoding         кодировка
   * @param classesToBeBound классы контекста преобразования
   * @return xml-элемент
   */
  public static String toString(Object data, QName qName, String encoding, Class... classesToBeBound) {
    try {
      Marshaller jaxbMarshaller = getMarshaller(data.getClass(), encoding, null, null, null, classesToBeBound);
      StringWriter sw = new StringWriter();
      jaxbMarshaller.marshal(getJAXBElement(qName, data), sw);
      return sw.toString();
    } catch (Exception e) {
      throw new JPXmlMarshalException(String.format("Ошибка преобразования класса в xml-строку: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Преобразует объект класса в xml-строку с указанными префиксами пространств имен
   *
   * @param data              объект класса
   * @param encoding          кодировка
   * @param namespacePrefixes маппинг URI пространств имен на их префиксы (k: namespace, v: prefix)
   * @param classesToBeBound  классы контекста преобразования
   * @return xml-элемент
   */
  @SuppressWarnings("rawtypes")
  public static String toString(Object data, String encoding, Map<String, String> namespacePrefixes, Class... classesToBeBound) {
    try {
      Marshaller jaxbMarshaller = getMarshaller(data.getClass(), encoding, null, null, namespacePrefixes, classesToBeBound);
      StringWriter sw = new StringWriter();
      jaxbMarshaller.marshal(data, sw);
      return sw.toString();
    } catch (Exception e) {
      throw new JPXmlMarshalException(String.format("Ошибка преобразования класса в xml-строку: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Преобразует объект класса в xml-строку с указанными префиксами пространств имен
   *
   * @param data              объект класса
   * @param qName             квалификационное имя результирующего xml-элемента
   * @param encoding          кодировка
   * @param namespacePrefixes маппинг URI пространств имен на их префиксы (k: namespace, v: prefix)
   * @param classesToBeBound  классы контекста преобразования
   * @return xml-элемент
   */
  public static String toString(Object data, QName qName, String encoding, Map<String, String> namespacePrefixes, Class... classesToBeBound) {
    try {
      Marshaller jaxbMarshaller = getMarshaller(data.getClass(), encoding, null, null, namespacePrefixes, classesToBeBound);
      StringWriter sw = new StringWriter();
      jaxbMarshaller.marshal(getJAXBElement(qName, data), sw);
      return sw.toString();
    } catch (Exception e) {
      throw new JPXmlMarshalException(String.format("Ошибка преобразования класса в xml-строку: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Преобразует объект класса в xml-элемент
   *
   * @param data             объект класса
   * @param qName            квалификационное имя результирующего xml-элемента
   * @param schema           схема данных
   * @param eventHandler     обработчик событий валидации
   * @param classesToBeBound классы контекста преобразования
   * @return xml-элемент
   */
  public static Element marshalToElement(Object data, QName qName, Resource schema, ValidationEventHandler eventHandler, Class... classesToBeBound) {
    if (schema == null) {
      throw new JPXmlMarshalException("Не удалось получить XSD-схему для валидации");
    }
    try {
      Marshaller jaxbMarshaller = getMarshaller(data.getClass(), schema, eventHandler, classesToBeBound);
      Document document = JPDocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
      jaxbMarshaller.marshal(getJAXBElement(qName, data), document);
      return document.getDocumentElement();
    } catch (Exception e) {
      throw new JPXmlMarshalException(String.format("Ошибка преобразования класса в xml-элемент: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Преобразует объект класса в xml-элемент с указанными префиксами пространств имен
   *
   * @param data              объект класса
   * @param qName             квалификационное имя результирующего xml-элемента
   * @param schema            схема данных
   * @param eventHandler      обработчик событий валидации
   * @param namespacePrefixes маппинг URI пространств имен на их префиксы (k: namespace, v: prefix)
   * @param classesToBeBound  классы контекста преобразования
   * @return xml-элемент
   */
  public static Element marshalToElement(Object data, QName qName, Resource schema, ValidationEventHandler eventHandler, Map<String, String> namespacePrefixes, Class... classesToBeBound) {
    if (schema == null) {
      throw new JPXmlMarshalException("Не удалось получить XSD-схему для валидации");
    }
    try {
      Marshaller jaxbMarshaller = getMarshaller(data.getClass(), null, schema, eventHandler, namespacePrefixes, classesToBeBound);
      Document document = JPDocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
      jaxbMarshaller.marshal(getJAXBElement(qName, data), document);
      return document.getDocumentElement();
    } catch (Exception e) {
      throw new JPXmlMarshalException(String.format("Ошибка преобразования класса в xml-элемент: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Преобразует объект класса в xml-элемент
   *
   * @param destination      назначение
   * @param data             объект класса
   * @param qName            квалификационное имя результирующего xml-элемента
   * @param schema           схема данных
   * @param eventHandler     обработчик событий валидации
   * @param classesToBeBound классы контекста преобразования
   * @return xml-элемент
   */
  public static <T> void marshalToElement(Node destination, T data, QName qName, Resource schema, ValidationEventHandler eventHandler, Class... classesToBeBound) {
    if (schema == null) {
      throw new JPXmlMarshalException("Не удалось получить XSD-схему для валидации");
    }
    try {
      Marshaller jaxbMarshaller = getMarshaller(data.getClass(), schema, eventHandler, classesToBeBound);
      jaxbMarshaller.marshal(getJAXBElement(qName, data), destination);
    } catch (Exception e) {
      throw new JPXmlMarshalException(String.format("Ошибка преобразования класса в xml-элемент: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Преобразует объект класса в xml-элемент
   *
   * @param data             объект класса
   * @param qName            квалификационное имя результирующего xml-элемента
   * @param classesToBeBound классы контекста преобразования
   * @return xml-элемент
   */
  public static Element marshalToElement(Object data, QName qName, Class... classesToBeBound) {
    try {
      Marshaller jaxbMarshaller = getMarshaller(data.getClass(), null, null, classesToBeBound);
      Document document = JPDocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
      jaxbMarshaller.marshal(getJAXBElement(qName, data), document);
      return document.getDocumentElement();
    } catch (Exception e) {
      throw new JPXmlMarshalException(String.format("Ошибка преобразования класса в xml-элемент: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Преобразует объект класса в xml-элемент
   *
   * @param destination      назначение
   * @param data             объект класса
   * @param qName            квалификационное имя результирующего xml-элемента
   * @param classesToBeBound классы контекста преобразования
   * @return xml-элемент
   */
  public static void marshalToElement(Node destination, Object data, QName qName, Class... classesToBeBound) {
    try {
      Marshaller jaxbMarshaller = getMarshaller(data.getClass(), null, null, classesToBeBound);
      jaxbMarshaller.marshal(getJAXBElement(qName, data), destination);
    } catch (Exception e) {
      throw new JPXmlMarshalException(String.format("Ошибка преобразования класса в xml-элемент: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Преобразует поток байтов в xml-элемент
   *
   * @param stream поток байтов
   * @return xml-элемент
   */
  public static Element marshalToElement(InputStream stream) {
    try {
      return JPDocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream).getDocumentElement();
    } catch (Exception e) {
      throw new JPXmlMarshalException(String.format("Ошибка преобразования потока байтов в xml-элемент: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Преобразует массив байтов в xml-элемент
   *
   * @param data массив байтов
   * @return xml-элемент
   */
  public static Element marshalToElement(byte[] data) {
    try (ByteArrayInputStream bos = new ByteArrayInputStream(data)) {
      return JPDocumentBuilderFactory.newInstance().newDocumentBuilder().parse(bos).getDocumentElement();
    } catch (Exception e) {
      throw new JPXmlMarshalException(String.format("Ошибка преобразования потока байтов в xml-элемент: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Преобразует массив байтов в xml-элемент без учета пространств имен
   *
   * @param data массив байтов
   * @return xml-элемент
   */
  public static Element marshalToUnqualifiedElement(byte[] data) {
    try (ByteArrayInputStream bos = new ByteArrayInputStream(data)) {
      return JPDocumentBuilderFactory.newUnqualifiedInstance().newDocumentBuilder().parse(bos).getDocumentElement();
    } catch (Exception e) {
      throw new JPXmlMarshalException(String.format("Ошибка преобразования потока байтов в xml-элемент: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Преобразует массив байтов в xml-элемент без учета пространств имен
   *
   * @param is поток байтов
   * @return xml-элемент
   */
  public static Element marshalToUnqualifiedElement(InputStream is) {
    try {
      return JPDocumentBuilderFactory.newUnqualifiedInstance().newDocumentBuilder().parse(is).getDocumentElement();
    } catch (Exception e) {
      throw new JPXmlMarshalException(String.format("Ошибка преобразования потока байтов в xml-элемент: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Преобразует строку в xml-элемент
   *
   * @param xmlStr строка
   * @return xml-элемент
   */
  public static Element marshalToElement(String xmlStr) {
    if (xmlStr == null || xmlStr.isEmpty()) {
      return null;
    }
    try {
      return JPDocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(xmlStr))).getDocumentElement();
    } catch (Exception e) {
      throw new JPXmlMarshalException(String.format("Ошибка преобразования строки в xml-элемент: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Преобразует объект класса в массив байтов
   *
   * @param data             объект класса
   * @param encoding         кодировка
   * @param qName            квалификационное имя результирующего xml-элемента
   * @param schema           схема данных
   * @param eventHandler     обработчик событий валидации
   * @param classesToBeBound классы контекста преобразования
   * @return массив байтов
   */
  public static byte[] marshalToByteArray(Object data, String encoding, QName qName, Resource schema, ValidationEventHandler eventHandler, Class... classesToBeBound) {
    if (schema == null) {
      throw new JPXmlMarshalException("Не удалось получить XSD-схему для валидации");
    }
    try {
      Marshaller jaxbMarshaller = getMarshaller(data.getClass(), encoding, schema, eventHandler, null, classesToBeBound);
      try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
        jaxbMarshaller.marshal(getJAXBElement(qName, data), bos);
        return bos.toByteArray();
      }
    } catch (Exception e) {
      throw new JPXmlMarshalException(String.format("Ошибка преобразования класса в xml-элемент: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Преобразует объект класса в массив байтов с указанными префиксами пространств имен
   *
   * @param data              объект класса
   * @param encoding          кодировка
   * @param qName             квалификационное имя результирующего xml-элемента
   * @param schema            схема данных
   * @param eventHandler      обработчик событий валидации
   * @param namespacePrefixes маппинг URI пространств имен на их префиксы (k: namespace, v: prefix)
   * @param classesToBeBound  классы контекста преобразования
   * @return массив байтов
   */
  public static byte[] marshalToByteArray(Object data, String encoding, QName qName, Resource schema, ValidationEventHandler eventHandler, Map<String, String> namespacePrefixes, Class... classesToBeBound) {
    if (schema == null) {
      throw new JPXmlMarshalException("Не удалось получить XSD-схему для валидации");
    }
    try {
      Marshaller jaxbMarshaller = getMarshaller(data.getClass(), encoding, schema, eventHandler, namespacePrefixes, classesToBeBound);
      try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
        jaxbMarshaller.marshal(getJAXBElement(qName, data), bos);
        return bos.toByteArray();
      }
    } catch (Exception e) {
      throw new JPXmlMarshalException(String.format("Ошибка преобразования класса в xml-элемент: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Преобразует объект класса в массив байтов с указанными префиксами пространств имен
   *
   * @param data              объект класса
   * @param qName             квалификационное имя результирующего xml-элемента
   * @param schema            схема данных
   * @param eventHandler      обработчик событий валидации
   * @param namespacePrefixes маппинг URI пространств имен на их префиксы (k: namespace, v: prefix)
   * @param classesToBeBound  классы контекста преобразования
   * @return массив байтов
   */
  public static byte[] marshalToByteArray(Object data, QName qName, Resource schema, ValidationEventHandler eventHandler, Map<String, String> namespacePrefixes, Class... classesToBeBound) {
    if (schema == null) {
      throw new JPXmlMarshalException("Не удалось получить XSD-схему для валидации");
    }
    try {
      Marshaller jaxbMarshaller = getMarshaller(data.getClass(), null, schema, eventHandler, namespacePrefixes, classesToBeBound);
      try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
        jaxbMarshaller.marshal(getJAXBElement(qName, data), bos);
        return bos.toByteArray();
      }
    } catch (Exception e) {
      throw new JPXmlMarshalException(String.format("Ошибка преобразования класса в xml-элемент: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Преобразует объект класса в массив байтов
   *
   * @param data             объект класса
   * @param qName            квалификационное имя результирующего xml-элемента
   * @param scheme           схема данных
   * @param eventHandler     обработчик событий валидации
   * @param classesToBeBound классы контекста преобразования
   * @return массив байтов
   */
  public static byte[] marshalToByteArray(Object data, QName qName, Resource scheme, ValidationEventHandler eventHandler, Class... classesToBeBound) {
    return marshalToByteArray(data, null, qName, scheme, eventHandler, classesToBeBound);
  }

  /**
   * Преобразует объект класса в массив байтов
   *
   * @param data             объект класса
   * @param qName            квалификационное имя результирующего xml-элемента
   * @param classesToBeBound классы контекста преобразования
   * @return массив байтов
   */
  public static byte[] marshalToByteArray(Object data, QName qName, Class... classesToBeBound) {
    return marshalToByteArray(data, null, qName, classesToBeBound);
  }

  /**
   * Преобразует объект класса в массив байтов
   *
   * @param data             объект класса
   * @param encoding         кодировка
   * @param qName            квалификационное имя результирующего xml-элемента
   * @param classesToBeBound классы контекста преобразования
   * @return массив байтов
   */
  public static byte[] marshalToByteArray(Object data, String encoding, QName qName, Class... classesToBeBound) {
    try {
      Marshaller jaxbMarshaller = getMarshaller(data.getClass(), encoding,null,null, null, classesToBeBound);
      try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
        jaxbMarshaller.marshal(getJAXBElement(qName, data), bos);
        return bos.toByteArray();
      }
    } catch (Exception e) {
      throw new JPXmlMarshalException(String.format("Ошибка преобразования класса в xml-элемент: \n%s", e.getMessage()), e);
    }
  }

  /**
   * Преобразует объект класса в массив байтов
   *
   * @param data             объект класса
   * @param encoding         кодировка
   * @param qName            квалификационное имя результирующего xml-элемента
   * @param namespacePrefixes маппинг URI пространств имен на их префиксы (k: namespace, v: prefix)
   * @param classesToBeBound классы контекста преобразования
   * @return массив байтов
   */
  public static byte[] marshalToByteArray(Object data, String encoding, QName qName, Map<String, String> namespacePrefixes, Class... classesToBeBound) {
    try {
      Marshaller jaxbMarshaller = getMarshaller(data.getClass(), encoding,null,null, namespacePrefixes, classesToBeBound);
      try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
        jaxbMarshaller.marshal(getJAXBElement(qName, data), bos);
        return bos.toByteArray();
      }
    } catch (Exception e) {
      throw new JPXmlMarshalException(String.format("Ошибка преобразования класса в xml-элемент: \n%s", e.getMessage()), e);
    }
  }

  public static Marshaller getMarshaller(Class dataClass, Resource scheme, ValidationEventHandler eventHandler, Class... classesToBeBound) throws Exception {
    return getMarshaller(dataClass, null, scheme, eventHandler, null, classesToBeBound);
  }

  private static Marshaller getMarshaller(Class dataClass, String encoding, Resource scheme, ValidationEventHandler eventHandler, Map<String, String> namespacePrefixes, Class... classesToBeBound) throws Exception {
    JAXBContext jaxbContext = classesToBeBound != null && classesToBeBound.length > 0 ? JAXBContext.newInstance(classesToBeBound) : JAXBContext.newInstance(dataClass);
    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

    if (encoding != null) {
      jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
    }

    if (namespacePrefixes != null && !namespacePrefixes.isEmpty()) {
      // Создаем анонимный класс NamespacePrefixMapper для управления префиксами
      NamespacePrefixMapper prefixMapper = new NamespacePrefixMapper() {
        @Override
        public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
          String prefix = namespacePrefixes.get(namespaceUri);
          if (prefix != null) {
            return prefix;
          }
          return suggestion;
        }

        @Override
        public String[] getPreDeclaredNamespaceUris() {
          return namespacePrefixes.keySet().toArray(new String[0]);
        }
      };

      // Устанавливаем маппер префиксов
      jaxbMarshaller.setProperty("org.glassfish.jaxb.namespacePrefixMapper", prefixMapper);
    }

    if (scheme != null) {
      jaxbMarshaller.setSchema(JPSchemaFactory.getSchema(scheme));
      if (eventHandler != null) {
        jaxbMarshaller.setEventHandler(eventHandler);
      }
    }
    return jaxbMarshaller;
  }

  @SuppressWarnings("unchecked")
  public static <T> JAXBElement<T> getJAXBElement(QName qName, T data) {
    return new JAXBElement<>(qName, (Class<T>) data.getClass(), data);
  }
}
