package mp.jprime.security.jwt;

import mp.jprime.security.AuthInfo;

import java.util.Date;

/**
 * Данные токена
 */
public interface JWTInfo extends AuthInfo {
  /**
   * Дата жизни токена
   *
   * @return Дата жизни токена
   */
  Date getExpirationDate();

  /**
   * Признак истечения жизни токена
   *
   * @return Да/Нет
   */
  boolean isExpired();

  /**
   * Признак валидности токена
   *
   * @return Да/Нет
   */
  boolean isValidate();
}
