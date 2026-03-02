package mp.jprime.xml.factories;

import mp.jprime.xml.exceptions.JPXsdValidationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Фабрика XSD-схемы
 */
public final class JPSchemaFactory {
  private JPSchemaFactory() {
  }

  /**
   * Получить объект правил XSD
   *
   * @param scheme расположение схемы
   * @return объект правил XSD
   */
  public static Schema getSchema(Resource scheme) {
    try {
      return getSchema(scheme.getURL());
    } catch (IOException e) {
      throw new JPXsdValidationException(e);
    }
  }

  /**
   * Получить объект правил XSD
   *
   * @param schemas XSD-схемы
   * @return объект правил XSD
   */
  public static Schema getSchema(Map<String, byte[]> schemas) {
    if (schemas == null || schemas.isEmpty()) {
      return null;
    }

    try {
      Collection<Source> sources = new ArrayList<>();
      for (Map.Entry<String, byte[]> entry : schemas.entrySet()) {
        sources.add(new StreamSource(new ByteArrayInputStream(entry.getValue())));
      }

      return setResourceResolver(newSecuritySchemaFactory(), schemas).newSchema(sources.toArray(new Source[0]));
    } catch (Exception e) {
      throw new JPXsdValidationException("Error parsing xsd schemas to validate. " + e.getMessage(), e);
    }
  }

  /**
   * Получить схемы из zip-архива
   *
   * @param zipStream zip-архив
   * @return схемы
   */
  public static Map<String, byte[]> getSchemaSources(InputStream zipStream) {
    Map<String, byte[]> schemas = new HashMap<>();
    try (ZipInputStream zis = new ZipInputStream(zipStream)) {
      ZipEntry entry;
      while ((entry = zis.getNextEntry()) != null) {
        if (entry.getName().endsWith("xsd")) {
          byte[] b = readContent(zis);
          schemas.put(JPSchemaFactory.getNamespace(b), b);
        }
      }
    } catch (Exception e) {
      throw new JPXsdValidationException("Error getting xsd schemas to validate request. " + e.getMessage(), e);
    }
    return schemas;
  }

  /**
   * Получить namespace из XSD
   *
   * @param schema XSD
   * @return namespace
   * @throws Exception ошибка обработки
   */
  public static String getNamespace(byte[] schema) throws Exception {
    DocumentBuilder builder = JPDocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document doc = builder.parse(new ByteArrayInputStream(schema));

    Node schemaNode = doc.getDocumentElement();
    NamedNodeMap attributes = schemaNode.getAttributes();
    Node targetNamespaceAttr = attributes.getNamedItem("targetNamespace");
    String targetNamespace = targetNamespaceAttr.getNodeValue();
    if (StringUtils.isBlank(targetNamespace)) {
      throw new IllegalStateException("Don't found targetNamespace");
    }
    return targetNamespace;
  }

  private static Schema getSchema(URL schema) {
    if (schema == null) {
      return null;
    }
    try {
      return newSecuritySchemaFactory().newSchema(schema);
    } catch (SAXParseException e) {
      throw new JPXsdValidationException("Ошибка получения xsd-правил{" + e.getSystemId() + "}: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new JPXsdValidationException("Ошибка получения xsd-правил: " + e.getMessage(), e);
    }
  }

  private static byte[] readContent(ZipInputStream zis) {
    byte[] contents = new byte[8192];
    try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
      int direct;
      while ((direct = zis.read(contents, 0, contents.length)) >= 0) {
        buffer.write(contents, 0, direct);
      }
      return buffer.toByteArray();
    } catch (IOException e) {
      throw new JPXsdValidationException("Error reading content from xsd schemas to validate request. " + e.getMessage(), e);
    }
  }

  private static SchemaFactory newSecuritySchemaFactory() {
    SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    try {
      schemaFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    } catch (Exception ignored) {
      // Свойства могут быть недоступны для некоторых SAX-фабрик — это нормально
    }
    try {
      schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    } catch (Exception ignored) {
      // Свойства могут быть недоступны для некоторых SAX-фабрик — это нормально
    }
    try {
      // протоколы по которым разрешена загрузка import схем
      schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "file,jar,nested");
    } catch (Exception ignored) {
      // Свойства могут быть недоступны для некоторых SAX-фабрик — это нормально
    }

    try {
      schemaFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    } catch (Exception ignored) {
      // Свойства могут быть недоступны для некоторых SAX-фабрик — это нормально
    }
    // factory.setFeature("http://xml.org/sax/features/external-general-entities", false); // unsupported
    // factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false); // unsupported
    return schemaFactory;
  }

  private static SchemaFactory setResourceResolver(SchemaFactory schemaFactory, Map<String, byte[]> schemas) {
    schemaFactory.setResourceResolver(new RXLSResourceResolver(schemas));
    return schemaFactory;
  }

  /**
   * Вспомогательный класс для перенаправления ссылок на внешние(относительно xsd которая в процессе парсинга) ресурсы.
   * <p/>
   * Для валидации используется архив с xsd схемами для определенного Вида сведений. В архиве может быть от 1 до N xsd схем.
   * В процессе создания Schema (класс содержащий все xsd схемы из архива), из архива по очереди выгружаются xsd и добавляются в Schema.
   * Порядок их загрузки важен и влияет на успех валидации.
   * <p/>
   * <i>
   * Но может случиться такой кейс:<br/>
   * a.xsd вложена в b.xsd.
   * Если b.xsd будет загружена раньше чем a.xsd. То валидация закончится ошибкой. Так как внутри b.xsd есть импорт a.xsd, а она еще не загружена.
   * </i>
   * <p/>
   * Именно для того, чтобы не заботится порядком загрузки схем используется данный класс.<br/>
   * Он заранее загружает все xsd схемы в мапу с ключом-namespace(соответствующим xsd схеме).
   * <p>
   * <p/><i>
   * Продолжая пример выше:<br/>
   * И если при загрузке b.xsd, a.xsd еще не будет загружена и соответственно найдена, то метод resourceResolver()
   * выдаст уже имеющийся у него a.xsd.
   * </i>
   */
  private record RXLSResourceResolver(Map<String, byte[]> schemas) implements LSResourceResolver {
    @Override
    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
      if (schemas.containsKey(namespaceURI)) {
        return new RXLSInput(systemId, baseURI, publicId, schemas.get(namespaceURI));
      }
      throw new JPXsdValidationException("Not found imported xsd schema with namespace={" + namespaceURI + "}.");
    }
  }

  /**
   * Объект обертка для xsd схемы
   */
  private record RXLSInput(String systemId, String baseUri, String publicId, byte[] schema) implements LSInput {
    @Override
    public Reader getCharacterStream() {
      return null;
    }

    @Override
    public void setCharacterStream(Reader characterStream) {

    }

    @Override
    public InputStream getByteStream() {
      return new ByteArrayInputStream(schema);
    }

    @Override
    public void setByteStream(InputStream byteStream) {

    }

    @Override
    public String getStringData() {
      return null;
    }

    @Override
    public void setStringData(String stringData) {

    }

    @Override
    public String getSystemId() {
      return systemId;
    }

    @Override
    public void setSystemId(String systemId) {

    }

    @Override
    public String getPublicId() {
      return publicId;
    }

    @Override
    public void setPublicId(String publicId) {

    }

    @Override
    public String getBaseURI() {
      return baseUri;
    }

    @Override
    public void setBaseURI(String baseURI) {

    }

    @Override
    public String getEncoding() {
      return StandardCharsets.UTF_8.name();
    }

    @Override
    public void setEncoding(String encoding) {

    }

    @Override
    public boolean getCertifiedText() {
      return false;
    }

    @Override
    public void setCertifiedText(boolean certifiedText) {

    }
  }
}
