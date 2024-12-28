package mp.jprime.security.abac;

import mp.jprime.loaders.JPDynamicLoader;

import java.util.Collection;

/**
 * Динамическая загрузка ABAC
 */
public interface JPAbacDynamicLoader extends JPDynamicLoader<Collection<PolicySet>> {

}
