package mp.jprime.security.services;

import mp.jprime.security.annotations.services.JPSecurityAnnoLoader;
import mp.jprime.security.xmlloader.services.JPSecurityXmlLoader;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static mp.jprime.security.Role.AUTH_ACCESS;

@RunWith(SpringRunner.class)
@ContextConfiguration()
public class JPSecurityManagerTest {
  @Autowired
  private JPSecurityStorage securityManager;

  @Configuration
  @ComponentScan(basePackages = {"mp.jprime.security"},
      useDefaultFilters=false,
      includeFilters = @ComponentScan.Filter(
          type = FilterType.ASSIGNABLE_TYPE,
          classes = {JPSecurityStorage.class, JPSecurityAnnoLoader.class, JPSecurityXmlLoader.class }),
      excludeFilters = @ComponentScan.Filter(
          type = FilterType.ASSIGNABLE_TYPE,
          classes = {JPAccessService.class})
  )
  public static class Config {
  }


  @Test
  public void testAdmin() {
    String jpPackage = "main";
    Collection<String> roles = Collections.singleton("ADMIN");
    Assert.assertTrue(securityManager.checkRead(jpPackage, roles));
    Assert.assertTrue(securityManager.checkCreate(jpPackage, roles));
    Assert.assertTrue(securityManager.checkDelete(jpPackage, roles));
    Assert.assertTrue(securityManager.checkUpdate(jpPackage, roles));
  }

  @Test
  public void testAuthAccess() {
    String jpPackage = "main";
    Collection<String> roles = Collections.singleton(AUTH_ACCESS);
    Assert.assertTrue(securityManager.checkRead(jpPackage, roles));
    Assert.assertFalse(securityManager.checkCreate(jpPackage, roles));
    Assert.assertFalse(securityManager.checkDelete(jpPackage, roles));
    Assert.assertFalse(securityManager.checkUpdate(jpPackage, roles));
  }

  @Test
  public void testUser() {
    String jpPackage = "main";
    Collection<String> roles = Collections.singleton("USER");
    Assert.assertFalse(securityManager.checkRead(jpPackage, roles));
    Assert.assertFalse(securityManager.checkCreate(jpPackage, roles));
    Assert.assertFalse(securityManager.checkDelete(jpPackage, roles));
    Assert.assertFalse(securityManager.checkUpdate(jpPackage, roles));
  }

  @Test
  public void testAdminAndUser() {
    String jpPackage = "main";
    Collection<String> roles = Arrays.asList("ADMIN", "USER");
    Assert.assertTrue(securityManager.checkRead(jpPackage, roles));
    Assert.assertFalse(securityManager.checkCreate(jpPackage, roles));
    Assert.assertFalse(securityManager.checkDelete(jpPackage, roles));
    Assert.assertFalse(securityManager.checkUpdate(jpPackage, roles));
  }
}

