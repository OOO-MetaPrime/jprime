package mp.jprime.json.modules;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Добавление настроек в ObjectMapper
 */
public interface JPObjectMapperExpander {
  /**
   * Добавление настроек в ObjectMapper
   *
   * @param objectMapper ObjectMapper
   */
  void expand(ObjectMapper objectMapper);
}
