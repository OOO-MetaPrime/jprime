package mp.jprime.web;

import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.json.services.JPJsonMapper;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * RestTemplate
 */
@Service
public class JPRestTemplate {

  private final RestTemplate restTemplate;

  @Value("${jprime.web.rest.connectTimeout:10}")
  private int connectTimeout;

  @Value("${jprime.web.rest.readTimeout:60}")
  private int readTimeout;

  @Autowired
  public JPRestTemplate(JPJsonMapper jsonMapper) {
    try {
      SSLContextBuilder sslContext = new SSLContextBuilder();
      sslContext.loadTrustMaterial(null, new TrustAllStrategy());
      CloseableHttpClient httpClient = HttpClients.custom()
          .setSSLHostnameVerifier(new NoopHostnameVerifier())
          .setSSLContext(sslContext.build())
          .build();
      HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
      factory.setHttpClient(httpClient);

      this.restTemplate = new RestTemplateBuilder()
          .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
          .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
          .messageConverters(
              new StringHttpMessageConverter(StandardCharsets.UTF_8),
              new MappingJackson2HttpMessageConverter(jsonMapper.getObjectMapper())
          )
          .setConnectTimeout(Duration.ofSeconds(connectTimeout))
          .setReadTimeout(Duration.ofSeconds(readTimeout))
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
