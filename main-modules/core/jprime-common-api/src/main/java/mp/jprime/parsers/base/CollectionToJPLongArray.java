package mp.jprime.parsers.base;

import mp.jprime.lang.JPLongArray;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Collection -> JPLongArray
 */
@Service
public final class CollectionToJPLongArray extends BaseTypeParser<Collection, JPLongArray> {
  @Override
  public JPLongArray parse(Collection value) {
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
  public Class<Collection> getInputType() {
    return Collection.class;
  }

  @Override
  public Class<JPLongArray> getOutputType() {
    return JPLongArray.class;
  }
}
