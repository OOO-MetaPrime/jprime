package mp.jprime.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import mp.jprime.json.services.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class JacksonConfig implements JsonMapper {
  @Bean
  @Primary
  public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
    return OBJECT_MAPPER.copy()
        // Игнорируем пустые значения
        .setSerializationInclusion(JsonInclude.Include.USE_DEFAULTS);
  }

  @Bean
  Jackson2JsonEncoder jackson2JsonEncoder(ObjectMapper mapper) {
    return new Jackson2JsonEncoder(mapper);
  }

  @Bean
  Jackson2JsonDecoder jackson2JsonDecoder(ObjectMapper mapper) {
    return new Jackson2JsonDecoder(mapper);
  }

  @Bean
  WebFluxConfigurer webFluxConfigurer(Jackson2JsonEncoder encoder, Jackson2JsonDecoder decoder) {
    return new WebFluxConfigurer() {
      @Override
      public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        configurer.defaultCodecs().jackson2JsonEncoder(encoder);
        configurer.defaultCodecs().jackson2JsonDecoder(decoder);
      }
    };
  }
}
