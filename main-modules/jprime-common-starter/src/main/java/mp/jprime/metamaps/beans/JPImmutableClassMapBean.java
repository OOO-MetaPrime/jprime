package mp.jprime.metamaps.beans;

import mp.jprime.metamaps.JPAttrMap;
import mp.jprime.metamaps.JPClassMap;
import mp.jprime.metamaps.JPImmutableClassMap;

import java.util.Collection;

/**
 * Неизменяемый метакласс
 */
public final class JPImmutableClassMapBean implements JPImmutableClassMap {
  private final JPClassMap jpClassMap;

  private JPImmutableClassMapBean(JPClassMap jpClassMap) {
    this.jpClassMap = jpClassMap;
  }

  /**
   * Кодовое имя класса
   *
   * @return Кодовое имя класса
   */
  @Override
  public String getCode() {
    return jpClassMap.getCode();
  }

  /**
   * Кодовое имя хранилища
   *
   * @return Кодовое имя хранилища
   */
  @Override
  public String getStorage() {
    return jpClassMap.getStorage();
  }

  /**
   * Мап на БД
   *
   * @return Мап на БД
   */
  @Override
  public String getMap() {
    return jpClassMap.getMap();
  }

  /**
   * Маппинг атрибутов
   *
   * @return Маппинг атрибутов
   */
  @Override
  public Collection<JPAttrMap> getAttrs() {
    return jpClassMap.getAttrs();
  }

  /**
   * Маппинг атрибутов
   *
   * @return Маппинг атрибутов
   */
  @Override
  public JPAttrMap getAttr(String code) {
    return jpClassMap.getAttr(code);
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
   * Построитель JPClassMap
   */
  public static final class Builder {
    private JPClassMap jpClassMap;

    private Builder() {
    }

    /**
     * Создаем метаописание
     *
     * @return Метаописание
     */
    public JPImmutableClassMapBean build() {
      return new JPImmutableClassMapBean(jpClassMap);
    }

    /**
     * Мапинг класса
     *
     * @param jpClassMap Мапинг класса
     * @return Builder
     */
    public Builder jpClassMap(JPClassMap jpClassMap) {
      this.jpClassMap = jpClassMap;
      return this;
    }
  }
}
