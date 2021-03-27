package mp.jprime.web;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import mp.jprime.json.services.JPJsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 * WebClient
 */
@Service
public class JPWebClient {
  private Logger LOG = LoggerFactory.getLogger(JPWebClient.class);

  private final WebClient WEB_CLIENT;

  public JPWebClient(@Autowired JPJsonMapper jpJsonMapper) {
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
    WEB_CLIENT = WebClient.builder()
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .defaultHeader("Content-Type", MediaType.APPLICATION_JSON.toString())
        .defaultHeader("Accept", MediaType.APPLICATION_JSON.toString())
        .exchangeStrategies(ExchangeStrategies.builder()
            .codecs(configurer -> {
                  ClientCodecConfigurer.ClientDefaultCodecs codes = configurer.defaultCodecs();
                  codes.maxInMemorySize(1024 * 1024 * 10);
                  codes.jackson2JsonEncoder(new Jackson2JsonEncoder(jpJsonMapper.getObjectMapper(), MediaType.APPLICATION_JSON));
                  codes.jackson2JsonDecoder(new Jackson2JsonDecoder(jpJsonMapper.getObjectMapper(), MediaType.APPLICATION_JSON));
                }
            )
            .build()
        )
        .build();
  }

  public WebClient getWebClient() {
    return WEB_CLIENT;
  }
}
