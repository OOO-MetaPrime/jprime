package mp.jprime.web;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import mp.jprime.json.services.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 * WebClient
 */
public interface JPWebClient extends JsonMapper {
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
            .codecs(configurer -> {
                  ClientCodecConfigurer.ClientDefaultCodecs codes = configurer.defaultCodecs();
                  codes.maxInMemorySize(1024 * 1024 * 10);
                  codes.jackson2JsonEncoder(new Jackson2JsonEncoder(OBJECT_MAPPER, MediaType.APPLICATION_JSON));
                  codes.jackson2JsonDecoder(new Jackson2JsonDecoder(OBJECT_MAPPER, MediaType.APPLICATION_JSON));
                }
            )
            .build()
        )
        .build();
  }
}
