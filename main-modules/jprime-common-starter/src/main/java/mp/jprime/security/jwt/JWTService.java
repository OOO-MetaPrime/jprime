package mp.jprime.security.jwt;

import mp.jprime.security.AuthInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

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
   * @return Данные
   */
  default AuthInfo getAuthInfo(ServerWebExchange swe) {
    ServerHttpRequest r = swe != null ? swe.getRequest() : null;
    HttpHeaders headers = r != null ? r.getHeaders() : null;
    String token = headers != null ? headers.getFirst(HttpHeaders.AUTHORIZATION) : null;
    return token == null || !token.startsWith("Bearer ") ? null : getAuthInfo(token.substring(7), swe);
  }

  /**
   * Чтение данных токена
   *
   * @param token Токен
   * @return Данные
   */
  JWTInfo getJWTInfo(String token);
}
