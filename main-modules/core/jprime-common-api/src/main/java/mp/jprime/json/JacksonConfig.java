package mp.jprime.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import mp.jprime.json.services.JPJsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.codec.json.JacksonJsonDecoder;
import org.springframework.http.codec.json.JacksonJsonEncoder;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class JacksonConfig {
  private final JPJsonMapper jpJsonMapper;

  public JacksonConfig(@Autowired JPJsonMapper jpJsonMapper) {
    this.jpJsonMapper = jpJsonMapper;
  }

  @Bean
  @Primary
  public JsonMapper jsonMapper() {
    return jpJsonMapper.getObjectMapper().rebuild()
        // Игнорируем пустые значения
        .changeDefaultPropertyInclusion(incl -> incl
            .withValueInclusion(JsonInclude.Include.USE_DEFAULTS)
            .withContentInclusion(JsonInclude.Include.USE_DEFAULTS))
        .build();
  }

  @Bean
  @Primary
  JacksonJsonEncoder jacksonJsonEncoder(JPJsonMapper mapper) {
    return new JacksonJsonEncoder(mapper.getObjectMapper());
  }

  @Bean
  @Primary
  JacksonJsonDecoder jacksonJsonDecoder(JPJsonMapper mapper) {
    return new JacksonJsonDecoder(mapper.getObjectMapper());
  }
}
