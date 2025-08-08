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

  public JPStringArray parse(String value) {
    if (value == null) {
      return null;
    }
    if (value.startsWith("[")) {
      List<String> values = jsonMapper.toObject(TYPE_REF, value);
      return values == null || values.isEmpty() ? null : JPStringArray.of(values);
    }
    return JPStringArray.of(Collections.singletonList(value));
  }

  public Class<String> getInputType() {
    return String.class;
  }

  public Class<JPStringArray> getOutputType() {
    return JPStringArray.class;
  }
}
