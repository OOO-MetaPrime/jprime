package mp.jprime.utils.test.beans;

import mp.jprime.utils.JPUtil;
import mp.jprime.utils.annotations.JPUtilLink;
import mp.jprime.utils.annotations.JPUtilModeLink;
import reactor.core.publisher.Mono;

import static mp.jprime.security.Role.AUTH_ACCESS;

@JPUtilLink(
    authRoles = AUTH_ACCESS,
    code = "test/testutil",
    jpClasses = "testClass",
    title = "Тестовая утилита"
)
public class TestUtil implements JPUtil {
  @JPUtilModeLink(
      code = "print",
      title = "Тестовая утилита",
      outClass = Out.class
  )
  public Mono<Out> printData(In s) {
    return Mono.fromCallable(() -> Out.newInstance(s.getValue()));
  }
}
