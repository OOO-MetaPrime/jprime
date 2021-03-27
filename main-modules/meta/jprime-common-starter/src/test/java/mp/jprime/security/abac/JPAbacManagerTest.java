package mp.jprime.security.abac;

import mp.jprime.dataaccess.JPAction;
import mp.jprime.security.abac.services.JPAbacStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration()
class JPAbacManagerTest {
  @Autowired
  private JPAbacStorage securityManager;

  @Lazy(value = false)
  @Configuration
  @ComponentScan(basePackages = {"mp.jprime.security", "mp.jprime.meta", "mp.jprime.parsers", "mp.jprime.log"})
  public static class Config {
  }

  @Test
  void testLoadSettings() {
    assertEquals(2, securityManager.getSettings().size());
    assertEquals(2, securityManager.getSettings("test1", JPAction.CREATE).size());
    assertEquals(2, securityManager.getSettings("test1", JPAction.READ).size());
    assertEquals(2, securityManager.getSettings("test1", JPAction.UPDATE).size());
  }
}

