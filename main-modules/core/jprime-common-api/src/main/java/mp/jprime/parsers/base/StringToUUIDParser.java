package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * String -> UUID
 */
@Service
public final class StringToUUIDParser extends BaseTypeParser<String, UUID> {
  @Override
  public UUID parse(String value) {
    if (value == null) {
      return null;
    }
    return UUID.fromString(value);
  }

  @Override
  public Class<String> getInputType() {
    return String.class;
  }

  @Override
  public Class<UUID> getOutputType() {
    return UUID.class;
  }
}
