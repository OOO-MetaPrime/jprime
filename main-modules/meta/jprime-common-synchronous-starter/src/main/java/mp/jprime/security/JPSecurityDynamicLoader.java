package mp.jprime.security;

import mp.jprime.loaders.JPDynamicLoader;

import java.util.Collection;

/**
 * Динамическая загрузка настроек доступа
 */
public interface JPSecurityDynamicLoader extends JPDynamicLoader<Collection<JPSecurityPackage>> {

}
