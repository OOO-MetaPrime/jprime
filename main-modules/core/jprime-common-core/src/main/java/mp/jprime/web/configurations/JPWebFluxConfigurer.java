package mp.jprime.web.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.PathMatchConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableWebFlux
public class JPWebFluxConfigurer implements WebFluxConfigurer {
  @Override
  public void configurePathMatching(PathMatchConfigurer configurer) {
    configurer.setUseTrailingSlashMatch(true);
  }
}