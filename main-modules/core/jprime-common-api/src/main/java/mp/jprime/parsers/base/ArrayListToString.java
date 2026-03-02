package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * ArrayList -> String
 */
@Service
public final class ArrayListToString extends BaseTypeParser<ArrayList, String> {
  @Override
  public String parse(ArrayList value) {
    if (value == null) {
      return null;
    }
    return ((Collection<Object>) value).stream().map(x -> parserService.toString(x))
        .collect(Collectors.joining(","));
  }

  @Override
  public Class<ArrayList> getInputType() {
    return ArrayList.class;
  }

  @Override
  public Class<String> getOutputType() {
    return String.class;
  }
}
