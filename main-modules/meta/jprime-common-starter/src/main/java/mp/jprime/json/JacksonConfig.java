package mp.jprime.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import mp.jprime.json.services.JPJsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;

@Lazy(value = false)
@Configuration
public class JacksonConfig {
  private Logger LOG = LoggerFactory.getLogger(JacksonConfig.class);
  private JPJsonMapper jpJsonMapper;

  @Autowired
  private void setJpJsonMapper(JPJsonMapper jpJsonMapper) {
    this.jpJsonMapper = jpJsonMapper;
  }

  @Bean
  @Primary
  public ObjectMapper objectMapper() {
    return jpJsonMapper.getObjectMapper().copy()
        // Игнорируем пустые значения
        .setSerializationInclusion(JsonInclude.Include.USE_DEFAULTS);
  }

  @Bean
  @Primary
  Jackson2JsonEncoder jackson2JsonEncoder(ObjectMapper mapper) {
    return new Jackson2JsonEncoder(mapper);
  }

  @Bean
  @Primary
  Jackson2JsonDecoder jackson2JsonDecoder(ObjectMapper mapper) {
    return new Jackson2JsonDecoder(mapper);
  }
}
