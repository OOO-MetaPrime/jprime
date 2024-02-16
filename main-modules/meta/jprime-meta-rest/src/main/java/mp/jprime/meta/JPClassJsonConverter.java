package mp.jprime.meta;

import mp.jprime.meta.json.beans.*;

import java.util.Collection;

/**
 * Json конвертер меты
 */
public interface JPClassJsonConverter {
  /**
   * Конвертация {@link JPClass} -> {@link JsonJPClass}
   */
  JsonJPClass toJson(JPClass jpClass);

  /**
   * Конвертация {@link JPAttr} -> {@link JsonJPAttr}
   */
  JsonJPAttr toJson(JPAttr jpAttr);

  /**
   * Конвертация {@link JPFile} -> {@link JsonJPFile}
   */
  JsonJPFile toJson(JPFile jpFile);

  /**
   * Конвертация {@link JPSimpleFraction} -> {@link JsonJPSimpleFraction}
   */
  JsonJPSimpleFraction toJson(JPSimpleFraction simpleFraction);

  /**
   * Конвертация {@link JPMoney} -> {@link JsonJPMoney}
   */
  JsonJPMoney toJson(JPMoney money);

  /**
   * Конвертация {@link JPGeometry} -> {@link JsonJPGeometry}
   */
  JsonJPGeometry toJson(JPGeometry geometry);

  /**
   * Конвертация {@link JPVirtualPath} -> {@link JsonJPVirtualPath}
   */
  JsonJPVirtualPath toJson(JPVirtualPath virtualPath);

  /**
   * Конвертация коллекции {@link JPProperty} -> {@link JsonJPProperty}
   */
  Collection<JsonJPProperty> toJsonJPProperty(Collection<JPProperty> properties);
}
