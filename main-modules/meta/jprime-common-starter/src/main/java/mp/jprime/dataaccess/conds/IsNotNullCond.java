package mp.jprime.dataaccess.conds;

import mp.jprime.dataaccess.enums.FilterOperation;

import java.util.Collection;

public class IsNotNullCond implements CollectionCond<String> {

  public IsNotNullCond() {
  }

  public static IsNotNullCond newInstance() {
    return new IsNotNullCond();
  }

  @Override
  public Collection<String> getValue() {
    return null;
  }

  @Override
  public boolean check(Collection<String> value) {
    return false;
  }

  @Override
  public boolean check(String value) {
    return true;
  }

  @Override
  public FilterOperation getOper() {
    return FilterOperation.ISNOTNULL;
  }
}
