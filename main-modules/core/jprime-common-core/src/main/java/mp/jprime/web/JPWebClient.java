package mp.jprime.web;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import mp.jprime.json.services.JPJsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

/**
 * WebClient
 */
@Service
public class JPWebClient {
  private static final Logger LOG = LoggerFactory.getLogger(JPWebClient.class);

  public static final int MAX_IN_MEMORY_SIZE = 1024 * 1024 * 10;

  private final WebClient WEB_CLIENT;

  public JPWebClient(@Autowired JPJsonMapper jpJsonMapper) {
    HttpClient httpClient = null;
    try {
      SslContext sslContext = SslContextBuilder
          .forClient()
          .trustManager(InsecureTrustManagerFactory.INSTANCE)
          .build();

      // Настройка для отключения существующего канала и созданию нового через 10 минут
      ConnectionProvider provider = ConnectionProvider.builder("jpWebClient")
          .maxConnections(500) // + увеличим на всякой случай количество соединений до 500
          .pendingAcquireTimeout(Duration.ofSeconds(45))
          .maxIdleTime(Duration.ofSeconds(600)).build();

      httpClient = HttpClient.create(provider)
          .secure(
              t -> t.sslContext(sslContext)
          )
          .compress(true);
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
    WEB_CLIENT = WebClient.builder()
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString())
        .exchangeStrategies(ExchangeStrategies.builder()
            .codecs(configurer -> {
                  ClientCodecConfigurer.ClientDefaultCodecs codes = configurer.defaultCodecs();
                  codes.maxInMemorySize(MAX_IN_MEMORY_SIZE);
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
