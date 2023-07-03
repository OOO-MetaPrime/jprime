package mp.jprime.security.abac.beans;

import mp.jprime.security.abac.PolicyTarget;

import java.util.Collection;
import java.util.Collections;

/**
 * Условие - метаописание класса
 */
public class PolicyTargetBean implements PolicyTarget {
  private final Collection<String> jpClasses;

  private PolicyTargetBean(Collection<String> jpClasses) {
    this.jpClasses = jpClasses != null ? Collections.unmodifiableCollection(jpClasses) : Collections.emptyList();
  }

  /**
   * Значение
   *
   * @return Значение
   */
  @Override
  public Collection<String> getJpClassCodes() {
    return jpClasses;
  }

  public static PolicyTargetBean from(Collection<String> jpClasses) {
    return new PolicyTargetBean(jpClasses);
  }
}

