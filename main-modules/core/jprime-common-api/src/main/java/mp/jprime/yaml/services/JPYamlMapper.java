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
  private static ObjectMapper yamlMapper;

  private JPYamlMapper() {
    yamlMapper = YAMLMapper.builder().build();
  }

  @Override
  public ObjectMapper getObjectMapper() {
    return yamlMapper;
  }

  /**
   * Получить {@link YAMLMapper}
   *
   * @return {@link YAMLMapper}
   */
  public static ObjectMapper getMapper() {
    return yamlMapper;
  }
}
