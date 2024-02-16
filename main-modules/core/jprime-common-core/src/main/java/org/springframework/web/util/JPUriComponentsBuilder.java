package org.springframework.web.util;

import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.server.ServerRequest;

/**
 * Пришлось делать свою обертку, так как adaptFromForwardedHeaders в UriComponentsBuilder пакетной видимости
 */
public final class JPUriComponentsBuilder {

  /**
   * Create a new {@code UriComponents} object from the URI associated with
   * the given HttpRequest while also overlaying with values from the headers
   * "Forwarded" (<a href="https://tools.ietf.org/html/rfc7239">RFC 7239</a>),
   * or "X-Forwarded-Host", "X-Forwarded-Port", and "X-Forwarded-Proto" if
   * "Forwarded" is not found.
   *
   * @param request the source request
   * @return the URI components of the URI
   * @since 4.1.5
   */
  public static UriComponentsBuilder fromServerRequest(ServerRequest request) {
    return ForwardedHeaderUtils.adaptFromForwardedHeaders(request.uri(), request.headers().asHttpHeaders());
  }

  /**
   * Create a new {@code UriComponents} object from the URI associated with
   * the given HttpRequest while also overlaying with values from the headers
   * "Forwarded" (<a href="https://tools.ietf.org/html/rfc7239">RFC 7239</a>),
   * or "X-Forwarded-Host", "X-Forwarded-Port", and "X-Forwarded-Proto" if
   * "Forwarded" is not found.
   *
   * @param request the source request
   * @return the URI components of the URI
   * @since 4.1.5
   */
  public static UriComponentsBuilder fromServerRequest(ServerRequest request, String path) {
    HttpHeaders httpHeaders = request.headers().asHttpHeaders();
    String prefix = httpHeaders.getFirst("X-Forwarded-Prefix");

    return ForwardedHeaderUtils.adaptFromForwardedHeaders(request.uri(), request.headers().asHttpHeaders())
        .replacePath((prefix != null ? "/" + String.join("", prefix.split(",")) : "") + path);
  }
}
