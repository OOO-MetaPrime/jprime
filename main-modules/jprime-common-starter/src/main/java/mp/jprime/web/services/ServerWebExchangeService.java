package mp.jprime.web.services;

import org.springframework.web.server.ServerWebExchange;

public interface ServerWebExchangeService {
  /**
   * Возвращает IP адрес клиента
   *
   * @return IP адрес клиента
   */
  String getRemoteHost(ServerWebExchange e);

  /**
   * Возвращает baseUrl
   *
   * @param e ServerWebExchange
   * @return baseUrl
   */
  String getBaseUrl(ServerWebExchange e);
}
