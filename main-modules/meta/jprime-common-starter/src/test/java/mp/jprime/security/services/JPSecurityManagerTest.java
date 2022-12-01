package mp.jprime.security.services;

import mp.jprime.security.annotations.services.JPSecurityAnnoLoader;
import mp.jprime.security.xmlloader.services.JPSecurityXmlLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static mp.jprime.security.Role.AUTH_ACCESS;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JPSecurityManagerTest.Config.class)
class JPSecurityManagerTest {
  @Autowired
  private JPSecurityStorage securityManager;

  @Lazy(value = false)
  @Configuration
  @ComponentScan(basePackages = {"mp.jprime.security"},
      useDefaultFilters = false,
      includeFilters = @ComponentScan.Filter(
          type = FilterType.ASSIGNABLE_TYPE,
          classes = {JPSecurityStorage.class, JPSecurityAnnoLoader.class, JPSecurityXmlLoader.class}),
      excludeFilters = @ComponentScan.Filter(
          type = FilterType.ASSIGNABLE_TYPE,
          classes = {JPResourceAccessService.class})
  )
  public static class Config {
  }


  @Test
  void testAdmin() {
    String jpPackage = "main";
    Collection<String> roles = Collections.singleton("ADMIN");
    assertTrue(securityManager.checkRead(jpPackage, roles));
    assertTrue(securityManager.checkCreate(jpPackage, roles));
    assertTrue(securityManager.checkDelete(jpPackage, roles));
    assertTrue(securityManager.checkUpdate(jpPackage, roles));
  }

  @Test
  void testAdminWrongPackage() {
    String jpPackage = "main_error";
    Collection<String> roles = Collections.singleton("ADMIN");
    assertFalse(securityManager.checkRead(jpPackage, roles));
    assertFalse(securityManager.checkCreate(jpPackage, roles));
    assertFalse(securityManager.checkDelete(jpPackage, roles));
    assertFalse(securityManager.checkUpdate(jpPackage, roles));
  }

  @Test
  void testAuthAccess() {
    String jpPackage = "main";
    Collection<String> roles = Collections.singleton(AUTH_ACCESS);
    assertTrue(securityManager.checkRead(jpPackage, roles));
    assertFalse(securityManager.checkCreate(jpPackage, roles));
    assertFalse(securityManager.checkDelete(jpPackage, roles));
    assertFalse(securityManager.checkUpdate(jpPackage, roles));
  }

  @Test
  void testUser() {
    String jpPackage = "main";
    Collection<String> roles = Collections.singleton("USER");
    assertFalse(securityManager.checkRead(jpPackage, roles));
    assertFalse(securityManager.checkCreate(jpPackage, roles));
    assertFalse(securityManager.checkDelete(jpPackage, roles));
    assertFalse(securityManager.checkUpdate(jpPackage, roles));
  }

  @Test
  void testAdminAndUser() {
    String jpPackage = "main";
    Collection<String> roles = Arrays.asList("ADMIN", "USER");
    assertTrue(securityManager.checkRead(jpPackage, roles));
    assertFalse(securityManager.checkCreate(jpPackage, roles));
    assertFalse(securityManager.checkDelete(jpPackage, roles));
    assertFalse(securityManager.checkUpdate(jpPackage, roles));
  }
}

