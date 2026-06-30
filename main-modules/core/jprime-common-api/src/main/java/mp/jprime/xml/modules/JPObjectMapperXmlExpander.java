package mp.jprime.xml.modules;

import tools.jackson.databind.cfg.MapperBuilder;

/**
 * Добавление настроек в ObjectMapper
 */
public interface JPObjectMapperXmlExpander {
  /**
   * Добавление настроек в MapperBuilder
   *
   * @param builder MapperBuilder
   */
  void expand(MapperBuilder<?, ?> builder);
}
