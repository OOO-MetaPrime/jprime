package mp.jprime.parsers.base;

import com.fasterxml.jackson.core.type.TypeReference;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.lang.JPLongArray;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * String -> JPLongArray
 */
@Service
public final class StringToJPLongArray extends BaseTypeParser<String, JPLongArray> {
  private final TypeReference<List<Long>> TYPE_REF = new TypeReference<>() {
  };

  private final JPJsonMapper jsonMapper;

  private StringToJPLongArray(@Autowired JPJsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  public JPLongArray parse(String value) {
    if (value == null) {
      return null;
    }
    if (value.startsWith("[")) {
      List<Long> values = jsonMapper.toObject(TYPE_REF, value);
      return values == null || values.isEmpty() ? null : JPLongArray.of(values);
    }
    return JPLongArray.of(Collections.singletonList(parserService.parseTo(Long.class, value)));
  }

  public Class<String> getInputType() {
    return String.class;
  }

  public Class<JPLongArray> getOutputType() {
    return JPLongArray.class;
  }
}
