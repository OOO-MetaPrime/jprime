package mp.jprime.configurations;

import mp.jprime.web.JPWebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.codec.multipart.MultipartHttpMessageReader;
import org.springframework.http.codec.multipart.SynchronossPartHttpMessageReader;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurationSupport;
import org.synchronoss.cloud.nio.multipart.DefaultPartBodyStreamStorageFactory;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@Configuration
@Lazy(value = false)
public class JPWebFluxConfig extends WebFluxConfigurationSupport {
  private static final Logger LOG = LoggerFactory.getLogger(JPWebFluxConfig.class);
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
    SynchronossPartHttpMessageReader reader = new SynchronossPartHttpMessageReader();
    configurer.customCodecs().register(reader);
    ServerCodecConfigurer.ServerDefaultCodecs codecs = configurer.defaultCodecs();
    codecs.jackson2JsonEncoder(encoder);
    codecs.jackson2JsonDecoder(decoder);
    codecs.maxInMemorySize(JPWebClient.MAX_IN_MEMORY_SIZE);
    codecs.multipartReader(new MultipartHttpMessageReader(reader));
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
   *
   * @param event событие формирования контекста
   */
  @EventListener
  public void handleContextRefreshedEvent(ContextRefreshedEvent event) {
    DefaultPartBodyStreamStorageFactory factory = new DefaultPartBodyStreamStorageFactory();
  }
}
