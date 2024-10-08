package mp.jprime.yaml.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import mp.jprime.json.services.JPBaseObjectMapper;
import org.springframework.stereotype.Service;

/**
 * Yaml-обработчик
 */
@Service
public class JPYamlMapper extends JPBaseObjectMapper {
  private static ObjectMapper YAML_MAPPER;

  private JPYamlMapper() {
    YAML_MAPPER = YAMLMapper.builder().build();
  }

  @Override
  public ObjectMapper getObjectMapper() {
    return YAML_MAPPER;
  }

  /**
   * Получить {@link YAMLMapper}
   *
   * @return {@link YAMLMapper}
   */
  public static ObjectMapper getMapper() {
    return YAML_MAPPER;
  }
}
