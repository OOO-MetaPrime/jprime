package mp.jprime.parsers.base;

import com.fasterxml.jackson.core.type.TypeReference;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.lang.JPIntegerArray;
import mp.jprime.parsers.ParserService;
import mp.jprime.parsers.ParserServiceAware;
import mp.jprime.parsers.TypeParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * String -> JPIntegerArray
 */
@Service
public final class StringToJPIntegerArray implements TypeParser<String, JPIntegerArray>, ParserServiceAware {
  private final TypeReference<List<Integer>> TYPE_REF = new TypeReference<List<Integer>>() {
  };

  private JPJsonMapper jpJsonMapper;
  private ParserService service;

  @Autowired
  private void setJPJsonMapper(JPJsonMapper jpJsonMapper) {
    this.jpJsonMapper = jpJsonMapper;
  }

  @Override
  public void setParserService(ParserService service) {
    this.service = service;
  }

  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public JPIntegerArray parse(String value) {
    if (value == null) {
      return null;
    }
    if (value.startsWith("[")) {
      return JPIntegerArray.of(jpJsonMapper.toObject(TYPE_REF, value));
    }
    return JPIntegerArray.of(Collections.singletonList(service.parseTo(Integer.class, value)));
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
  public Class<JPIntegerArray> getOutputType() {
    return JPIntegerArray.class;
  }
}
