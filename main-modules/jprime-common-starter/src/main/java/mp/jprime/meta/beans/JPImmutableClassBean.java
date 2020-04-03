package mp.jprime.meta.beans;

import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPImmutableClass;

import java.util.Collection;
import java.util.Objects;

/**
 * Неизменяемый метакласс
 */
public final class JPImmutableClassBean implements JPImmutableClass {
  private final JPClass jpClass;

  private JPImmutableClassBean(JPClass jpClass) {
    this.jpClass = jpClass;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    JPClassBean jpClass = (JPClassBean) o;
    return Objects.equals(jpClass.getCode(), getCode());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getCode());
  }

  @Override
  public String getGuid() {
    return jpClass.getGuid();
  }

  @Override
  public String getCode() {
    return jpClass.getCode();
  }

  @Override
  public String getPluralCode() {
    return jpClass.getPluralCode();
  }

  @Override
  public String getName() {
    return jpClass.getName();
  }

  @Override
  public String getShortName() {
    return jpClass.getShortName();
  }

  @Override
  public String getDescription() {
    return jpClass.getDescription();
  }

  @Override
  public String getQName() {
    return jpClass.getQName();
  }

  @Override
  public String getJpPackage() {
    return jpClass.getJpPackage();
  }

  @Override
  public boolean isInner() {
    return jpClass.isInner();
  }

  @Override
  public Collection<JPAttr> getAttrs() {
    return jpClass.getAttrs();
  }

  @Override
  public JPAttr getAttr(String code) {
    return jpClass.getAttr(code);
  }

  @Override
  public JPAttr getPrimaryKeyAttr() {
    return jpClass.getPrimaryKeyAttr();
  }

  /**
   * Построитель JPImmutableClassBean
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель JPClass
   */
  public static final class Builder {
    private JPClass jpClass;

    private Builder() {
    }

    /**
     * Создаем метаописание
     *
     * @return Метаописание
     */
    public JPImmutableClassBean build() {
      return new JPImmutableClassBean(jpClass);
    }

    /**
     * Метаописание класса
     *
     * @param jpClass Метаописание класса
     * @return Builder
     */
    public Builder jpClass(JPClass jpClass) {
      this.jpClass = jpClass;
      return this;
    }
  }
}
