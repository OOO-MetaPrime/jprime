package mp.jprime.configurations;

import mp.jprime.utils.JPApplicationStartListener;
import mp.jprime.web.JPWebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurationSupport;
import org.synchronoss.cloud.nio.multipart.DefaultPartBodyStreamStorageFactory;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@Configuration
public class JPWebFluxConfig extends WebFluxConfigurationSupport  implements JPApplicationStartListener {
  private Jackson2JsonEncoder encoder;
  private Jackson2JsonDecoder decoder;

  @Autowired
  private void setEncoder(Jackson2JsonEncoder encoder) {
    this.encoder = encoder;
  }

  @Autowired
  private void setDecoder(Jackson2JsonDecoder decoder) {
    this.decoder = decoder;
  }

  @Override
  public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
    ServerCodecConfigurer.ServerDefaultCodecs codecs = configurer.defaultCodecs();
    codecs.jackson2JsonEncoder(encoder);
    codecs.jackson2JsonDecoder(decoder);
    codecs.maxInMemorySize(JPWebClient.MAX_IN_MEMORY_SIZE);
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping("/**")
        .allowedOrigins("*")
        .allowedMethods("*")
        .allowedHeaders("*");
  }

  /**
   * При старте сервиса вызывает конструктор  {@link DefaultPartBodyStreamStorageFactory},
   * который создает временную папку для загружаемых файлов во избежание состояния "гонки"
   */
  @Override
  public void applicationStart() {
    DefaultPartBodyStreamStorageFactory factory = new DefaultPartBodyStreamStorageFactory();
  }
}