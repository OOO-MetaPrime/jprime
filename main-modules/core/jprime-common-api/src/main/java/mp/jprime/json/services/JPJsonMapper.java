package mp.jprime.json.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import mp.jprime.json.modules.JPObjectMapperExpander;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Базовый класс JSON-обработчиков
 */
@Service
public class JPJsonMapper extends JPObjectMapper {
  private static ObjectMapper OBJECT_MAPPER;

  private JPJsonMapper(@Autowired Collection<JPObjectMapperExpander> expanders) {
    OBJECT_MAPPER = new ObjectMapper();
    setSettings(expanders, OBJECT_MAPPER);
  }

  @Override
  public ObjectMapper getObjectMapper() {
    return OBJECT_MAPPER;
  }

  /**
   * ObjectMapper
   *
   * @return ObjectMapper
   */
  public static ObjectMapper getMapper() {
    return OBJECT_MAPPER;
  }
}
