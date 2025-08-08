package mp.jprime.utils.testutils;

import mp.jprime.reactor.core.publisher.JPMono;
import mp.jprime.utils.JPUtil;
import mp.jprime.utils.annotations.JPUtilLink;
import mp.jprime.utils.annotations.JPUtilModeLink;
import reactor.core.publisher.Mono;

import static mp.jprime.security.Role.AUTH_ACCESS;

/**
 * Тестовая утилита
 */
@JPUtilLink(
    authRoles = AUTH_ACCESS,
    code = "hello",
    title = "Тестовая утилита"
)
public class HelloUtil implements JPUtil {
  @JPUtilModeLink(
      code = "response",
      title = "Тестовая утилита",
      outClass = Out.class
  )
  public Mono<Out> printData(In s) {
    return JPMono.fromCallable(() -> Out.newInstance("Thanx for " + s.getRequest()));
  }
}
