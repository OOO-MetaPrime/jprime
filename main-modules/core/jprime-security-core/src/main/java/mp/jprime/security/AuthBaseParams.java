package mp.jprime.security;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Общая логика AuthParams
 */
public abstract class AuthBaseParams implements AuthParams {
  private volatile Collection<String> oktmoPrefixList;
  private volatile Collection<String> oktmoTreeList;
  private volatile Map<String, Object> props;

  @Override
  public Collection<String> getOktmoPrefixList() {
    if (oktmoPrefixList == null) {
      oktmoPrefixList = AuthParams.super.getOktmoPrefixList();
    }
    return oktmoPrefixList;
  }

  @Override
  public Collection<String> getOktmoTreeList() {
    if (oktmoTreeList == null) {
      oktmoTreeList = AuthParams.super.getOktmoTreeList();
    }
    return oktmoTreeList;
  }

  @Override
  public Object getProperty(String key, Supplier<Object> func) {
    if (func == null || key == null) {
      return null;
    }

    if (props == null) {
      synchronized (this) {
        if (props == null) {
          props = new ConcurrentHashMap<>();
        }
      }
    }
    return props.computeIfAbsent(key, x -> func.get());
  }
}
