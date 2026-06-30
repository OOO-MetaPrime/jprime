package mp.jprime.configurations;

import mp.jprime.application.JPApplicationInitListener;
import mp.jprime.web.JPWebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.JacksonJsonDecoder;
import org.springframework.http.codec.json.JacksonJsonEncoder;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurationSupport;
import org.synchronoss.cloud.nio.multipart.DefaultPartBodyStreamStorageFactory;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@Configuration
public class JPWebFluxConfig extends WebFluxConfigurationSupport implements JPApplicationInitListener {
  private final JacksonJsonEncoder encoder;
  private final JacksonJsonDecoder decoder;

  public JPWebFluxConfig(@Autowired JacksonJsonEncoder encoder,
                         @Autowired JacksonJsonDecoder decoder) {
    this.encoder = encoder;
    this.decoder = decoder;
  }

  @Override
  public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
    ServerCodecConfigurer.ServerDefaultCodecs codecs = configurer.defaultCodecs();
    codecs.jacksonJsonEncoder(encoder);
    codecs.jacksonJsonDecoder(decoder);
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
  public void applicationInit() {
    DefaultPartBodyStreamStorageFactory factory = new DefaultPartBodyStreamStorageFactory();
  }
}