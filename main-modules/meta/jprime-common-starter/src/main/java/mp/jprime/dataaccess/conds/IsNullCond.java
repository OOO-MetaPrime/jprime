package mp.jprime.dataaccess.conds;

import mp.jprime.dataaccess.enums.FilterOperation;

import java.util.Collection;

/**
 * Условие
 */
public class IsNullCond implements CollectionCond<String> {

  private IsNullCond() {

  }

  public static IsNullCond newInstance() {
    return new IsNullCond();
  }

  /**
   * Значение
   *
   * @return Значение
   */
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

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.ISNULL;
  }
}
