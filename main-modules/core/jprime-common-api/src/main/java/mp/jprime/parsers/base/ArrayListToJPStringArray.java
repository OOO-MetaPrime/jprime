package mp.jprime.parsers.base;

import mp.jprime.lang.JPStringArray;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * ArrayList -> JPStringArray
 */
@Service
public final class ArrayListToJPStringArray extends BaseTypeParser<ArrayList, JPStringArray> {
  @Override
  public JPStringArray parse(ArrayList value) {
    if (value == null) {
      return null;
    }
    return JPStringArray.of(
        ((Collection<Object>) value).stream()
            .map(x -> parserService.parseTo(String.class, x))
            .collect(Collectors.toList())
    );
  }

  @Override
  public Class<ArrayList> getInputType() {
    return ArrayList.class;
  }

  @Override
  public Class<JPStringArray> getOutputType() {
    return JPStringArray.class;
  }
}
