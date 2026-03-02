package mp.jprime.parsers.base;

import mp.jprime.lang.JPStringArray;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Collection -> JPStringArray
 */
@Service
public final class CollectionToJPStringArray extends BaseTypeParser<Collection, JPStringArray> {
  @Override
  public JPStringArray parse(Collection value) {
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
  public Class<Collection> getInputType() {
    return Collection.class;
  }

  @Override
  public Class<JPStringArray> getOutputType() {
    return JPStringArray.class;
  }
}
