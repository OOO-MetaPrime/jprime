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
  private final TypeReference<List<Integer>> TYPE_REF = new TypeReference<>() {
  };

  private JPJsonMapper jsonMapper;
  private ParserService service;

  @Autowired
  private void setJPJsonMapper(JPJsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  @Override
  public void setParserService(ParserService service) {
    this.service = service;
  }

  public JPIntegerArray parse(String value) {
    if (value == null) {
      return null;
    }
    if (value.startsWith("[")) {
      List<Integer> values = jsonMapper.toObject(TYPE_REF, value);
      return values == null || values.isEmpty() ? null : JPIntegerArray.of(values);
    }
    return JPIntegerArray.of(Collections.singletonList(service.parseTo(Integer.class, value)));
  }

  public Class<String> getInputType() {
    return String.class;
  }

  public Class<JPIntegerArray> getOutputType() {
    return JPIntegerArray.class;
  }
}
