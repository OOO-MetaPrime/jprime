package mp.jprime.json.view.parsers;

import mp.jprime.json.view.helpers.MaskCardNumberValue;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * String -> MaskCardNumberValue
 */
@Service
public final class StringToMaskCardNumberValueParser extends BaseTypeParser<String, MaskCardNumberValue> {
  @Override
  public MaskCardNumberValue parse(String value) {
    return value == null || value.isEmpty() ? null : MaskCardNumberValue.of(value.trim());
  }

  @Override
  public Class<String> getInputType() {
    return String.class;
  }

  @Override
  public Class<MaskCardNumberValue> getOutputType() {
    return MaskCardNumberValue.class;
  }
}
