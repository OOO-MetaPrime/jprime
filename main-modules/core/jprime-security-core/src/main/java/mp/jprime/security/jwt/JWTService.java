package mp.jprime.security.jwt;

import mp.jprime.security.AuthInfo;
import mp.jprime.security.exceptions.JPBrokenTokenException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import java.util.Optional;

/**
 * Обработчик JWT
 */
public interface JWTService {
  /**
   * Чтение данных токена
   *
   * @param token Токен
   * @param swe   ServerWebExchange
   * @return Данные
   */
  AuthInfo getAuthInfo(String token, ServerWebExchange swe);

  /**
   * Чтение данных токена
   *
   * @param swe ServerWebExchange
   * @return Данные авторизации
   * @throws JPBrokenTokenException если не удается получить токен
   */
  default AuthInfo getAuthInfo(ServerWebExchange swe) {
    return getAuthInfoOptional(swe).orElseThrow(JPBrokenTokenException::new);
  }

  /**
   * Чтение данных токена
   *
   * @param swe ServerWebExchange
   * @return Данные авторизации
   */
  default Optional<AuthInfo> getAuthInfoOptional(ServerWebExchange swe) {
    return Optional.ofNullable(swe)
        .map(ServerWebExchange::getRequest)
        .map(ServerHttpRequest::getHeaders)
        .map(headers -> headers.getFirst(HttpHeaders.AUTHORIZATION))
        .filter(token -> token.startsWith("Bearer "))
        .map(token -> getAuthInfo(token.substring(7), swe));
  }

  /**
   * Чтение данных токена
   *
   * @param token Токен
   * @return Данные
   */
  JWTInfo getJWTInfo(String token);
}
