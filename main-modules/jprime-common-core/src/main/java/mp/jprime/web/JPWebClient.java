package mp.jprime.web;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 * WebClient
 */
public interface JPWebClient {
  Logger LOG = LoggerFactory.getLogger(JPWebClient.class);

  WebClient WEB_CLIENT = getWebClient();

  static WebClient getWebClient() {
    HttpClient httpClient = null;
    try {
      SslContext sslContext = SslContextBuilder
          .forClient()
          .trustManager(InsecureTrustManagerFactory.INSTANCE)
          .build();
      httpClient = HttpClient.create().secure(
          t -> t.sslContext(sslContext)
      );
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
    return WebClient.builder()
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .defaultHeader("Content-Type", MediaType.APPLICATION_JSON.toString())
        .defaultHeader("Accept", MediaType.APPLICATION_JSON.toString())
        .exchangeStrategies(ExchangeStrategies.builder()
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024 * 10)).build()
        )
        .build();
  }
}
