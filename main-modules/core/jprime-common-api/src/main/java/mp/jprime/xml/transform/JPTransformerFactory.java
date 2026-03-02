package mp.jprime.xml.transform;

import javax.xml.transform.TransformerFactory;

/**
 * Создание TransformerFactory
 */
public final class JPTransformerFactory {
  private final static String CLASS_NAME = "org.apache.xalan.processor.TransformerFactoryImpl";

  /**
   * Создание TransformerFactory по умолчанию
   * org.apache.xalan.processor.TransformerFactoryImpl
   *
   * @return TransformerFactory
   */
  public static TransformerFactory newInstance() {
    try {
      return TransformerFactory.newInstance(CLASS_NAME, null);
    } catch (Exception e) {
      System.setProperty("javax.xml.transform.TransformerFactory", CLASS_NAME);
      return TransformerFactory.newInstance();
    }
  }
}
