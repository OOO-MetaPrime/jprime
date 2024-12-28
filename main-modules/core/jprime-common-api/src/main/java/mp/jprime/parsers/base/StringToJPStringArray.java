package mp.jprime.parsers.base;

import com.fasterxml.jackson.core.type.TypeReference;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.lang.JPStringArray;
import mp.jprime.parsers.TypeParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * String -> JPStringArray
 */
@Service
public final class StringToJPStringArray implements TypeParser<String, JPStringArray> {
  private final TypeReference<List<String>> TYPE_REF = new TypeReference<>() {};

  private JPJsonMapper jsonMapper;

  @Autowired
  private void setJPJsonMapper(JPJsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public JPStringArray parse(String value) {
    if (value == null) {
      return null;
    }
    if (value.startsWith("[")) {
      return JPStringArray.of(jsonMapper.toObject(TYPE_REF, value));
    }
    return JPStringArray.of(Collections.singletonList(value));
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<String> getInputType() {
    return String.class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  public Class<JPStringArray> getOutputType() {
    return JPStringArray.class;
  }
}
