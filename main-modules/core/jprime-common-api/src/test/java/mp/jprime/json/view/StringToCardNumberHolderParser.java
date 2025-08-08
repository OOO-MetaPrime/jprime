package mp.jprime.json.view;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * String -> JsonGisecertECertificate.CardNumberValueHolder
 */
@Service
public final class StringToCardNumberHolderParser implements TypeParser<String, TestEntity.CardNumberValueHolder> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public TestEntity.CardNumberValueHolder parse(String value) {
    return value == null || value.isEmpty() ? null : TestEntity.CardNumberValueHolder.of(value.trim());
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<String> getInputType() {
    return String.class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  public Class<TestEntity.CardNumberValueHolder> getOutputType() {
    return TestEntity.CardNumberValueHolder.class;
  }
}
