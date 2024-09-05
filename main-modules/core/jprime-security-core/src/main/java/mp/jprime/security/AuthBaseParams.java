package mp.jprime.security;

import java.util.Collection;

/**
 * Общая логика AuthParams
 */
public abstract class AuthBaseParams implements AuthParams {
  private volatile Collection<String> oktmoPrefixList;
  private volatile Collection<String> oktmoTreeList;

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
}
