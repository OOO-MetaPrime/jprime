package mp.jprime.web.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class JPUIStaticRouter {
  @Bean
  RouterFunction<ServerResponse> staticResourceRouter() {
    return RouterFunctions.resources("/ui/**", new ClassPathResource("static/"));
  }
}
