package mp.jprime.globalsettings.beans;

import mp.jprime.globalsettings.JpGlobalPropertyCatalog;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * Каталог глобальный настроек
 */
public final class JpGlobalPropertyCatalogBean implements JpGlobalPropertyCatalog {
  /**
   * Код каталога
   */
  private final String catalog;
  /**
   * Название каталога
   */
  private final String name;
  /**
   * Роли доступа
   */
  private final Collection<String> roles;

  private JpGlobalPropertyCatalogBean(String catalog, String name, Collection<String> roles) {
    this.catalog = catalog;
    this.name = name;
    this.roles = roles != null ? new HashSet<>(roles) : Collections.emptyList();
  }

  public static JpGlobalPropertyCatalog of(String catalog, String name) {
    return of(catalog, name, null);
  }

  public static JpGlobalPropertyCatalog of(String catalog, String name, Collection<String> roles) {
    return new JpGlobalPropertyCatalogBean(catalog, name, roles);
  }

  @Override
  public String getCatalog() {
    return catalog;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Collection<String> getRoles() {
    return roles;
  }
}
