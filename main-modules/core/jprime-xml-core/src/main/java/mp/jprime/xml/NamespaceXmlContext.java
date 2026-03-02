package mp.jprime.xml;

import javax.xml.namespace.NamespaceContext;
import java.util.*;

/**
 * Класс, расширяющий функционал интерфейса NamespaceContext для регистрации и последующей
 * работы с пространствами имен xml-документа
 */
public final class NamespaceXmlContext implements NamespaceContext {

  private final Map<String, String> prefixToNamespace = new HashMap<>();

  private NamespaceXmlContext() {
  }

  @Override
  public String getNamespaceURI(String prefix) {
    return prefixToNamespace.getOrDefault(prefix, null);
  }

  @Override
  public String getPrefix(String namespaceURI) {
    return prefixToNamespace.entrySet()
        .stream()
        .filter(e -> Objects.equals(e.getValue(), namespaceURI))
        .map(Map.Entry::getKey)
        .findFirst()
        .orElse(null);
  }

  @Override
  public Iterator<String> getPrefixes(String namespaceURI) {
    List<String> prefixes = prefixToNamespace.entrySet().stream()
        .filter(e -> Objects.equals(e.getValue(), namespaceURI))
        .map(Map.Entry::getKey)
        .toList();

    return prefixes.iterator();
  }

  public static NamespaceXmlContext createContext() {
    return new NamespaceXmlContext();
  }

  /**
   * расширяет функционал интерфейса NamespaceContext добавляя функцию регистрации пространства имен
   *
   * @param prefix префикс пространства имен
   * @param uri    пространство имен
   */
  public NamespaceXmlContext register(String prefix, String uri) {
    prefixToNamespace.put(prefix, uri);
    return this;
  }

  /**
   * расширяет функционал интерфейса NamespaceContext добавляя функцию регистрации пространства имен
   *
   * @return возвращает Map в виде prefix->namespace
   */
  public Map<String, String> getPrefixMap() {
    return prefixToNamespace;
  }
}
