package mp.jprime.security.abac;

import mp.jprime.dataaccess.JPAction;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.security.abac.services.JPAbacMemoryStorage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JPJsonMapper.class, JPAbacManagerTest.Config.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JPAbacManagerTest {
  @Autowired
  private JPAbacMemoryStorage abacStorage;

  @Configuration
  @ComponentScan(basePackages = {
      "mp.jprime.security",
      "mp.jprime.meta",
      "mp.jprime.parsers",
      "mp.jprime.log"
  })
  public static class Config {
  }

  @BeforeAll
  void boot() {
    abacStorage.applicationBoot();
  }

  @Test
  void testLoadSettings() {
    assertEquals(2, abacStorage.getSettings().size());
    assertEquals(2, abacStorage.getSettings("test1", JPAction.CREATE).size());
    assertEquals(2, abacStorage.getSettings("test1", JPAction.READ).size());
    assertEquals(2, abacStorage.getSettings("test1", JPAction.UPDATE).size());
  }
}

