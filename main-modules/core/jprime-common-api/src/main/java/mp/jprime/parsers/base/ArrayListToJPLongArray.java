package mp.jprime.parsers.base;

import mp.jprime.lang.JPLongArray;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * ArrayList -> JPLongArray
 */
@Service
public final class ArrayListToJPLongArray extends BaseTypeParser<ArrayList, JPLongArray> {
  @Override
  public JPLongArray parse(ArrayList value) {
    if (value == null) {
      return null;
    }
    return JPLongArray.of(
        ((Collection<Object>) value).stream()
            .map(x -> parserService.parseTo(Long.class, x))
            .collect(Collectors.toList())
    );
  }

  @Override
  public Class<ArrayList> getInputType() {
    return ArrayList.class;
  }

  @Override
  public Class<JPLongArray> getOutputType() {
    return JPLongArray.class;
  }
}
