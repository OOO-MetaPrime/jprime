package mp.jprime.yaml.services;

import mp.jprime.json.services.JPBaseObjectMapper;
import org.springframework.stereotype.Service;
import tools.jackson.dataformat.yaml.YAMLMapper;

/**
 * Yaml-обработчик
 */
@Service
public class JPYamlMapper extends JPBaseObjectMapper {
  private static YAMLMapper YAML_MAPPER;

  private JPYamlMapper() {
    YAML_MAPPER = YAMLMapper.builder().build();
  }

  @Override
  public YAMLMapper getObjectMapper() {
    return YAML_MAPPER;
  }

  /**
   * Получить {@link YAMLMapper}
   *
   * @return {@link YAMLMapper}
   */
  public static YAMLMapper getMapper() {
    return YAML_MAPPER;
  }
}
