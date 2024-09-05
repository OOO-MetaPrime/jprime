package mp.jprime.web.services;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;

/**
 * Методы работы с ServerWebExchangeService
 */
@Service
public final class ServerWebExchangeCommonService implements ServerWebExchangeService {
  /**
   * Возвращает IP адрес клиента
   *
   * @return IP адрес клиента
   */
  @Override
  public String getRemoteHost(ServerWebExchange e) {
    ServerHttpRequest r = e == null ? null : e.getRequest();
    if (r == null) {
      return null;
    }
    HttpHeaders httpHeaders = r.getHeaders();
    InetSocketAddress isa = r.getRemoteAddress();
    InetAddress ia = isa != null ? isa.getAddress() : null;
    String remoteHost = ia != null ? ia.getHostAddress() : null;

    String xFor = httpHeaders.getFirst("X-Forwarded-For");
    return xFor != null ? xFor : remoteHost;
  }

  /**
   * Возвращает baseUrl
   *
   * @param e ServerWebExchange
   * @return baseUrl
   */
  @Override
  public String getBaseUrl(ServerWebExchange e) {
    ServerHttpRequest r = e == null ? null : e.getRequest();
    URI uri = r == null ? null : r.getURI();
    if (uri == null) {
      return null;
    }
    HttpHeaders httpHeaders = r.getHeaders();

    String xAPIScheme = httpHeaders.getFirst("X-Forwarded-APIService-Proto");
    String xScheme = httpHeaders.getFirst("X-Forwarded-Proto");
    String xHost = httpHeaders.getFirst("X-Forwarded-Host");
    String prefix = httpHeaders.getFirst("X-Forwarded-Prefix");

    String scheme = xAPIScheme != null ? xAPIScheme : (xScheme != null ? xScheme : uri.getScheme());
    String host = (xHost != null ? xHost : uri.getAuthority());
    return (scheme != null ? scheme + "://" : "") +
        (host != null ? host : "") +
        (prefix != null ? prefix : "");
  }
}
