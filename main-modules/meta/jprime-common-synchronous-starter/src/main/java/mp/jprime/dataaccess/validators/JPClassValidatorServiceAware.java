package mp.jprime.dataaccess.validators;

/**
 * Заполнение {@link JPClassValidatorService}
 */
public interface JPClassValidatorServiceAware {
  /**
   * Устанавливает   {@link JPClassValidatorService}
   *
   * @param validatorService {@link JPClassValidatorService}
   */
  void setJpClassValidatorService(JPClassValidatorService validatorService);
}
