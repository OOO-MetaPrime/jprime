package mp.jprime.exceptions.enums;

/**
 * Ошибки, связанные с ролями
 */
public enum JPRoleError {
  INVALID_ALLOW_ROLES("invalid_allowRoles", "Недоступен вход для активной учетной записи");

  // Код
  private final String code;
  // Название
  private final String name;

  JPRoleError(String code, String name) {
    this.code = code;
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }
}
