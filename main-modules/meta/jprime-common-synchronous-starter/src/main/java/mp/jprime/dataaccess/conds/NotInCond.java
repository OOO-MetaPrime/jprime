package mp.jprime.dataaccess.conds;

import mp.jprime.dataaccess.enums.FilterOperation;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;

/**
 * Условие NOT IN
 */
public class NotInCond implements CollectionCond<String> {
  private Collection<String> values;

  private NotInCond(Collection<String> value) {
    this.values = value != null ? Collections.unmodifiableCollection(value) : Collections.emptyList();
  }

  /**
   * Значение
   *
   * @return Значение
   */
  @Override
  public Collection<String> getValue() {
    return values;
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.NOT_IN;
  }

  @Override
  public boolean check(Collection<String> value) {
    return !CollectionUtils.containsAny(values, value);
  }

  @Override
  public boolean check(String value) {
    return !values.contains(value);
  }

  public static NotInCond from(Collection<String> value) {
    return new NotInCond(value);
  }
}
