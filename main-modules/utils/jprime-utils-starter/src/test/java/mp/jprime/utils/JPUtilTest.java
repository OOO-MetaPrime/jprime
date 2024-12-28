package mp.jprime.utils;

import mp.jprime.json.services.JsonJPObjectService;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.beans.AuthInfoBean;
import mp.jprime.security.services.JPSecurityStorage;
import mp.jprime.utils.test.beans.In;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static mp.jprime.security.Role.AUTH_ACCESS;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JPUtilTest.Config.class)
public class JPUtilTest {
  @Lazy(value = false)
  @Configuration
  @ComponentScan(
      basePackages = {
          "mp.jprime.log",
          "mp.jprime.json",
          "mp.jprime.parsers",
          "mp.jprime.utils"
      },
      excludeFilters = {
          @ComponentScan.Filter(
              type = FilterType.ASSIGNABLE_TYPE,
              value = {
                  JsonJPObjectService.class
              }
          )
      }
  )
  public static class Config {

  }

  @Autowired
  private JPUtilService jpUtilService;
  @MockitoBean
  private JPSecurityStorage jpSecurityStorage;
  @MockitoBean
  private JPMetaStorage jpMetaStorage;

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
