package mp.jprime.utils.testutils;

import mp.jprime.utils.JPUtil;
import mp.jprime.utils.annotations.JPUtilLink;
import mp.jprime.utils.annotations.JPUtilModeLink;
import reactor.core.publisher.Mono;

import static mp.jprime.security.Role.AUTH_ACCESS;

/**
 * Общая тестовая утилита
 */
@JPUtilLink(
    uni = true,
    authRoles = AUTH_ACCESS,
    code = "uni-hello",
    title = "Общая тестовая утилита"
)
public class UniHelloUtil implements JPUtil {
  @JPUtilModeLink(
      code = "response",
      title = "Общая тестовая утилита",
      outClass = UniOut.class
  )
  public Mono<UniOut> printData(UniIn s) {
    return Mono.fromCallable(() ->
        UniOut.newInstance("Thanx for " + s.getRequest() + " with class code '" + s.getObjectClassCode() + '\'')
    );
  }
}
