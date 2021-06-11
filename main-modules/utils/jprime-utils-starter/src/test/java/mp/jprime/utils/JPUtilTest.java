package mp.jprime.utils;

import mp.jprime.security.AuthInfo;
import mp.jprime.security.beans.AuthInfoBean;
import mp.jprime.security.services.JPSecurityStorage;
import mp.jprime.utils.services.JPUtilService;
import mp.jprime.utils.test.beans.In;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static mp.jprime.security.Role.AUTH_ACCESS;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration()
public class JPUtilTest {
  @Autowired
  private JPUtilService jpUtilService;

  @Lazy(value = false)
  @Configuration
  @ComponentScan(
      basePackages = {"mp.jprime.utils", "mp.jprime.log", "mp.jprime.json"}
  )
  public static class Config {
    @MockBean
    private JPSecurityStorage jpSecurityStorage;
  }

  @Test
  public void testTestUtilExists() {
    JPUtil util = jpUtilService.getUtil("test/testutil");
    assertNotNull(util);

    assertArrayEquals(util.getAuthRoles(), new String[]{AUTH_ACCESS});
    assertArrayEquals(util.getJpClasses(), new String[]{"testClass"});
  }

  @Test
  public void testTestUtilMethodWithBean() {
    AuthInfo authInfo = AuthInfoBean.newBuilder()
        .roles(Collections.singleton(AUTH_ACCESS))
        .build();
    assertEquals("custom", jpUtilService.apply("test/testutil", "print", In.newInstance("t"), authInfo).block().getResultType());
  }
}
