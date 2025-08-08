package mp.jprime.utils;

/**
 * Платежные системы
 */
public enum PaymentSystem {
  MIR("mir", "Мир", 16, 19);

  /**
   * Код платежной системы
   */
  private final String code;
  /**
   * Название платежной системы
   */
  private final String title;
  /**
   * Минимальная длина номера карты
   */
  private final int minLength;
  /**
   * Максимальная длина номера карты
   */
  private final int maxLength;

  PaymentSystem(String code, String title, int minLength, int maxLength) {
    this.code = code;
    this.title = title;
    this.minLength = minLength;
    this.maxLength = maxLength;
  }

  public String getCode() {
    return code;
  }

  public String getTitle() {
    return title;
  }

  public int getMinLength() {
    return minLength;
  }

  public int getMaxLength() {
    return maxLength;
  }
}
