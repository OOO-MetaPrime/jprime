package mp.jprime.xml.utils;

import org.w3c.dom.Node;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * XSLT-трансформация документа
 */
public final class XsltTransformsUtils {
  private XsltTransformsUtils() { }

  private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss");

  /**
   * XSLT-трансформация документа
   * @param xmlDocument xml-документ
   * @param template шаблон трансформации
   * @return результат
   * @throws TransformerException
   * @throws IOException
   */
  public static byte[] transform(Node xmlDocument, byte[] template) throws TransformerException, IOException {
    try (InputStream xslStream = new ByteArrayInputStream(template)) {
      return transform(xmlDocument, xslStream);
    }
  }

  /**
   * XSLT-трансформация документа
   * @param xmlDocument xml-документ
   * @param template шаблон трансформации
   * @return результат
   * @throws TransformerException
   * @throws IOException
   */
  public static byte[] transform(Node xmlDocument, InputStream template) throws TransformerException, IOException {
      return transform(xmlDocument, new StreamSource(template));
  }

  private static byte[] transform(Node xmlDocument, Source template) throws TransformerException, IOException {
    try (ByteArrayOutputStream resultStream = new ByteArrayOutputStream();) {
      Source inputXml = new DOMSource(xmlDocument);
      Result outputXml = new StreamResult(resultStream);

      Transformer transformer = TransformerFactory.newInstance().newTransformer(template);
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      transformer.setParameter("today", LocalDate.now().format(DateTimeFormatter.ISO_DATE));
      transformer.setParameter("timestamp", LocalDateTime.now().format(TIMESTAMP_FORMATTER));
      transformer.transform(inputXml, outputXml);
      return resultStream.toByteArray();
    }
  }

}
