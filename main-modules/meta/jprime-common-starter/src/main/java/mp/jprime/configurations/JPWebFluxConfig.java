package mp.jprime.configurations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

@Configuration
@Lazy(value = false)
public class JPWebFluxConfig extends WebFluxConfigurationSupport {
  private Logger LOG = LoggerFactory.getLogger(JPWebFluxConfig.class);
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
    configurer.defaultCodecs().jackson2JsonEncoder(encoder);
    configurer.defaultCodecs().jackson2JsonDecoder(decoder);
    configurer.defaultCodecs().multipartReader(new MultipartHttpMessageReader(reader));
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
