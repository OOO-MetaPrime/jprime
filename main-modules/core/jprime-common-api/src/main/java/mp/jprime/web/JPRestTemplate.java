package mp.jprime.web;

import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.xml.services.JPXmlMapper;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * RestTemplate
 */
@Service
public final class JPRestTemplate {
  private final RestTemplate restTemplate;

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

      CloseableHttpClient httpClient = HttpClients.custom()
          .setConnectionManager(connectionManager)
          .build();

      int connectTimeout = 10;
      int readTimeout = 20;

      HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
      factory.setHttpClient(httpClient);
      factory.setConnectTimeout(connectTimeout * 1000);
      factory.setReadTimeout(readTimeout * 1000);

      this.restTemplate = new RestTemplateBuilder()
          .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
          .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
          .messageConverters(
              new StringHttpMessageConverter(StandardCharsets.UTF_8),
              new MappingJackson2XmlHttpMessageConverter(xmlMapper.getObjectMapper()),
              new MappingJackson2HttpMessageConverter(jsonMapper.getObjectMapper())
          )
          .connectTimeout(Duration.ofSeconds(connectTimeout))
          .requestFactory(() -> factory)
          .build();
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  public RestTemplate getRestTemplate() {
    return restTemplate;
  }
}
