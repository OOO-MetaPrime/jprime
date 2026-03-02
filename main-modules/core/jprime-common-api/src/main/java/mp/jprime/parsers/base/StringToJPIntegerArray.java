package mp.jprime.parsers.base;

import com.fasterxml.jackson.core.type.TypeReference;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.lang.JPIntegerArray;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * String -> JPIntegerArray
 */
@Service
public final class StringToJPIntegerArray extends BaseTypeParser<String, JPIntegerArray> {
  private final TypeReference<List<Integer>> TYPE_REF = new TypeReference<>() {
  };

  private final JPJsonMapper jsonMapper;

  private StringToJPIntegerArray(@Autowired JPJsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  public JPIntegerArray parse(String value) {
    if (value == null) {
      return null;
    }
    if (value.startsWith("[")) {
      List<Integer> values = jsonMapper.toObject(TYPE_REF, value);
      return values == null || values.isEmpty() ? null : JPIntegerArray.of(values);
    }
    return JPIntegerArray.of(Collections.singletonList(parserService.parseTo(Integer.class, value)));
  }

  public Class<String> getInputType() {
    return String.class;
  }

  public Class<JPIntegerArray> getOutputType() {
    return JPIntegerArray.class;
  }
}
