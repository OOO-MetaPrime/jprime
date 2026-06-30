package mp.jprime.web.configurations;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.CacheControl;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class JPUIStaticRouter {

  @Value("${jprime.web.static.cache.maxAge:#{86400}}")
  private int maxAge;
  @Value("${jprime.web.static.cache.cachePublic:#{true}}")
  private boolean cachePublic;

  @Bean
  RouterFunction<ServerResponse> staticResourceRouter() {
    return RouterFunctions.resources("/ui/**", new ClassPathResource("static/"), (resource, headers) -> {
      if (maxAge > 0) {
        CacheControl cache = CacheControl.maxAge(Duration.ofSeconds(maxAge));
        if (cachePublic) {
          cache.cachePublic();
        }
        headers.setCacheControl(cache);
      }
    });
  }
}
