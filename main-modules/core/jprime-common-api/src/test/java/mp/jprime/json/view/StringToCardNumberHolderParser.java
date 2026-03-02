package mp.jprime.json.view;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * String -> JsonGisecertECertificate.CardNumberValueHolder
 */
@Service
public final class StringToCardNumberHolderParser extends BaseTypeParser<String, TestEntity.CardNumberValueHolder> {
  @Override
  public TestEntity.CardNumberValueHolder parse(String value) {
    return value == null || value.isEmpty() ? null : TestEntity.CardNumberValueHolder.of(value.trim());
  }

  @Override
  public Class<String> getInputType() {
    return String.class;
  }

  @Override
  public Class<TestEntity.CardNumberValueHolder> getOutputType() {
    return TestEntity.CardNumberValueHolder.class;
  }
}
