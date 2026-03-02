package mp.jprime.parsers.base;

import mp.jprime.lang.JPIntegerArray;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * ArrayList -> JPIntegerArray
 */
@Service
public final class ArrayListToJPIntegerArray extends BaseTypeParser<ArrayList, JPIntegerArray> {
  @Override
  public JPIntegerArray parse(ArrayList value) {
    if (value == null) {
      return null;
    }
    return JPIntegerArray.of(
        ((Collection<Object>) value).stream()
            .map(x -> parserService.parseTo(Integer.class, x))
            .collect(Collectors.toList())
    );
  }

  @Override
  public Class<ArrayList> getInputType() {
    return ArrayList.class;
  }

  @Override
  public Class<JPIntegerArray> getOutputType() {
    return JPIntegerArray.class;
  }
}
