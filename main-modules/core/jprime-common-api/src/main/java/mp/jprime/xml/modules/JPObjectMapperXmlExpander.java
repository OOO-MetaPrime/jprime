package mp.jprime.xml.modules;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Добавление настроек в ObjectMapper
 */
public interface JPObjectMapperXmlExpander {
  /**
   * Добавление настроек в ObjectMapper
   *
   * @param objectMapper ObjectMapper
   */
  void expand(ObjectMapper objectMapper);
}
