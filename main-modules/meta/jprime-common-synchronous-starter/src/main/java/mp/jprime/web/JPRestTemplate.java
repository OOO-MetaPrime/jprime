package mp.jprime.web;

import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.xml.services.JPXmlMapper;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.http.converter.xml.JacksonXmlHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * RestTemplate
 */
@Service
public final class JPRestTemplate {
  private final RestTemplate restTemplate;
  private final RestTemplate restExchangeTemplate;

  private JPRestTemplate(@Autowired JPJsonMapper jsonMapper,
                         @Autowired JPXmlMapper xmlMapper) {
    try {
      SSLContext sslContext = SSLContexts.custom()
          .loadTrustMaterial(null, (cert, authType) -> true)
          .build();

      PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
          RegistryBuilder.<ConnectionSocketFactory>create()
              .register("https", new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE))
              .register("http", new PlainConnectionSocketFactory())
              .build()
      );
      connectionManager.setMaxTotal(100);
      connectionManager.setDefaultMaxPerRoute(20);

      int connectTimeout = 10;
      int readTimeout = 20;

      RequestConfig requestConfig = RequestConfig.custom()
          .setConnectTimeout(Timeout.of(connectTimeout, TimeUnit.SECONDS))
          .setResponseTimeout(Timeout.of(readTimeout, TimeUnit.SECONDS))
          .setConnectionRequestTimeout(Timeout.of(readTimeout, TimeUnit.SECONDS))
          .build();

      CloseableHttpClient httpClient = HttpClients.custom()
          .setConnectionManager(connectionManager)
          .setDefaultRequestConfig(requestConfig)
          .build();

      HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);

      this.restTemplate = new RestTemplateBuilder()
          .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
          .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
          .messageConverters(
              new StringHttpMessageConverter(StandardCharsets.UTF_8),
              new JacksonXmlHttpMessageConverter(xmlMapper.getObjectMapper()),
              new JacksonJsonHttpMessageConverter(jsonMapper.getObjectMapper())
          )
          .requestFactory(() -> factory)
          .build();

      this.restExchangeTemplate = new RestTemplateBuilder()
          .requestFactory(() -> factory)
          .build();
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  public RestTemplate getRestTemplate() {
    return restTemplate;
  }

  /**
   * Запрос на скачивание файла
   *
   * @param entity RequestEntity
   * @return ResponseEntity<Resource
   */
  public ResponseEntity<Resource> exchange(RequestEntity<?> entity) {
    return restExchangeTemplate.exchange(entity, Resource.class);
  }
}
