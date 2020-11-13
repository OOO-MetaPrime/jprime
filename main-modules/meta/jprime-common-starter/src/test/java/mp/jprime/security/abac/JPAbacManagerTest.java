package mp.jprime.security.abac;

import mp.jprime.dataaccess.JPAction;
import mp.jprime.security.abac.services.JPAbacStorage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration()
public class JPAbacManagerTest {
  @Autowired
  private JPAbacStorage securityManager;

  @Configuration
  @ComponentScan(basePackages = {"mp.jprime.security", "mp.jprime.meta", "mp.jprime.parsers"})
  public static class Config {
  }

  @Test
  public void testLoadSettings() {
    Assert.assertEquals(2, securityManager.getSettings().size());
    Assert.assertEquals(2, securityManager.getSettings("test1", JPAction.CREATE).size());
    Assert.assertEquals(2, securityManager.getSettings("test1", JPAction.READ).size());
    Assert.assertEquals(2, securityManager.getSettings("test1", JPAction.UPDATE).size());
  }
}

