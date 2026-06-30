package mp.jprime.json.modules;

import tools.jackson.databind.cfg.MapperBuilder;

/**
 * Добавление настроек в ObjectMapper
 */
public interface JPObjectMapperExpander {
  /**
   * Добавление настроек в MapperBuilder
   *
   * @param builder MapperBuilder
   */
  void expand(MapperBuilder<?, ?> builder);
}
