package mp.jprime.json.view.helpers;

import mp.jprime.json.view.JsonViewValue;
import org.jetbrains.annotations.NotNull;

/**
 * Скрывает номер карты во view-json
 * Пользователю будет отображаться последние 4 цифры (************1234)
 * Пример:
 * @JsonViewProperty(value = "Номер карты МИР", toClass = MaskCardNumberValue.class)
 * private String cardNumber;
 */
public final class MaskCardNumberValue implements Comparable<MaskCardNumberValue> {
  private final String cardNumber;

  private MaskCardNumberValue(String cardNumber) {
    this.cardNumber = cardNumber;
  }

  public static MaskCardNumberValue of(String cardNumber) {
    return new MaskCardNumberValue(cardNumber);
  }

  public String getCardNumber() {
    return cardNumber;
  }

  @Override
  public int compareTo(@NotNull MaskCardNumberValue o) {
    if (cardNumber == null) {
      return -1;
    }
    if (o == null || o.cardNumber == null) {
      return 1;
    }
    return cardNumber.compareTo(o.cardNumber);
  }

  @JsonViewValue
  public String getMaskedCardNumber() {
    if (cardNumber == null || cardNumber.length() < 4) {
      return cardNumber;
    }
    StringBuilder maskedCard = new StringBuilder();
    for (int i = 0; i < cardNumber.length() - 4; i++) {
      maskedCard.append('*');
    }
    maskedCard.append(cardNumber.substring(cardNumber.length() - 4));
    return maskedCard.toString();
  }
}
