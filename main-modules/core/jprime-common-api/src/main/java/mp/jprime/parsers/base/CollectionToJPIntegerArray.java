package mp.jprime.parsers.base;

import mp.jprime.lang.JPIntegerArray;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Collection -> JPIntegerArray
 */
@Service
public final class CollectionToJPIntegerArray extends BaseTypeParser<Collection, JPIntegerArray> {
  @Override
  public JPIntegerArray parse(Collection value) {
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
  public Class<Collection> getInputType() {
    return Collection.class;
  }

  @Override
  public Class<JPIntegerArray> getOutputType() {
    return JPIntegerArray.class;
  }
}
