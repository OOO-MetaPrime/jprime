package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * UUID -> String
 */
@Service
public final class UUIDToStringParser extends BaseTypeParser<UUID, String> {
  @Override
  public String parse(UUID value) {
    if (value == null) {
      return null;
    }
    return value.toString();
  }

  @Override
  public Class<UUID> getInputType() {
    return UUID.class;
  }

  @Override
  public Class<String> getOutputType() {
    return String.class;
  }
}
