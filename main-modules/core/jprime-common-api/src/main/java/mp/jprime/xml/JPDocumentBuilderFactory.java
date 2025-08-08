package mp.jprime.xml;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Класс для создания экземпляров DocumentBuilderFactory с различными настройками безопасности и функциональностью.
 */
public final class JPDocumentBuilderFactory {
  private JPDocumentBuilderFactory() {}

  /**
   * Создает новый экземпляр DocumentBuilderFactory с безопасными настройками.
   *
   * @return Новый экземпляр DocumentBuilderFactory
   */
  public static DocumentBuilderFactory newInstance() {
    return getDocumentBuilderFactory(true, true, true, true);
  }

  /**
   * Создает новый экземпляр DocumentBuilderFactory без учета пространства имен и с безопасными настройками.
   *
   * @return Новый экземпляр DocumentBuilderFactory
   */
  public static DocumentBuilderFactory newUnqualifiedInstance() {
    return getDocumentBuilderFactory(false, true, true, true);
  }

  /**
   * Создает новый экземпляр DocumentBuilderFactory с указанными настройками и безопасными настройками.
   *
   * @param namespaceAware Указывает, следует ли учитывать пространство имен
   * @param coalescing Указывает, следует ли объединять смежные текстовые узлы
   * @param ignoringElementContentWhitespace Указывает, следует ли игнорировать пробелы между элементами
   * @return Новый экземпляр DocumentBuilderFactory
   */
  public static DocumentBuilderFactory newInstance(boolean namespaceAware, boolean coalescing, boolean ignoringElementContentWhitespace) {
    return getDocumentBuilderFactory(namespaceAware, coalescing, ignoringElementContentWhitespace, true);
  }

  /**
   * Создает новый небезопасный экземпляр DocumentBuilderFactory.
   *
   * @return Новый небезопасный экземпляр DocumentBuilderFactory
   */
  public static DocumentBuilderFactory newUnsecureInstance() {
    return getDocumentBuilderFactory(true, true, true, true);
  }

  /**
   * Создает новый небезопасный экземпляр DocumentBuilderFactory без учета пространства имен.
   *
   * @return Новый небезопасный экземпляр DocumentBuilderFactory
   */
  public static DocumentBuilderFactory newUnqualifiedUnsecureInstance() {
    return getDocumentBuilderFactory(false, true, true, true);
  }

  /**
   * Создает новый небезопасный экземпляр DocumentBuilderFactory с указанными настройками.
   *
   * @param namespaceAware Указывает, следует ли учитывать пространство имен
   * @param coalescing Указывает, следует ли объединять смежные текстовые узлы
   * @param ignoringElementContentWhitespace Указывает, следует ли игнорировать пробелы между элементами
   * @return Новый небезопасный экземпляр DocumentBuilderFactory
   */
  public static DocumentBuilderFactory newUnsecureInstance(boolean namespaceAware, boolean coalescing, boolean ignoringElementContentWhitespace) {
    return getDocumentBuilderFactory(namespaceAware, coalescing, ignoringElementContentWhitespace, false);
  }

  /**
   * Создает новый экземпляр DocumentBuilderFactory с указанными настройками и безопасностью.
   *
   * @param namespaceAware Указывает, следует ли учитывать пространство имен
   * @param coalescing Указывает, следует ли объединять смежные текстовые узлы
   * @param ignoringElementContentWhitespace Указывает, следует ли игнорировать пробелы между элементами
   * @param isSecure Указывает, следует ли применять безопасные настройки
   * @return Новый экземпляр DocumentBuilderFactory
   */
  private static DocumentBuilderFactory getDocumentBuilderFactory(boolean namespaceAware, boolean coalescing, boolean ignoringElementContentWhitespace, boolean isSecure) {
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    documentBuilderFactory.setNamespaceAware(namespaceAware);
    documentBuilderFactory.setCoalescing(coalescing);
    documentBuilderFactory.setIgnoringElementContentWhitespace(ignoringElementContentWhitespace);

    if (isSecure) {
      // Отключение поддержки внешних сущностей
      try {
        documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
      } catch (ParserConfigurationException e) {
        throw new RuntimeException("Failed to configure DocumentBuilderFactory", e);
      }

      // Отключение загрузки DTD
      documentBuilderFactory.setXIncludeAware(false);
      documentBuilderFactory.setExpandEntityReferences(false);
    }

    return documentBuilderFactory;
  }
}
