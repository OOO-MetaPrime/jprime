package mp.jprime.metamaps.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mp.jprime.metamaps.JPAttrMap;
import mp.jprime.metamaps.JPClassMap;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * описание привязки класса к хранилищу
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class JPClassMapBean implements JPClassMap {
  private final String code;
  private final String storage;
  private final String map;
  private final Map<String, JPAttrMap> attrs;
  private final Boolean immutable;

  /**
   * Конструктор
   *
   * @param code      Кодовое имя класса
   * @param storage   Кодовое имя хранилища
   * @param map       Мап на БД
   * @param attrs     Маппинг атрибутов
   * @param immutable Признак неизменяемости
   */
  private JPClassMapBean(String code, String storage, String map, Collection<JPAttrMap> attrs, boolean immutable) {
    this.immutable = immutable;

    this.code = code != null && !code.isEmpty() ? code : null;
    this.storage = storage != null && !storage.isEmpty() ? storage : null;
    this.map = map != null && !map.isEmpty() ? map : null;

    this.attrs = attrs == null ?
        Collections.emptyMap() : attrs.stream().collect(Collectors.toMap(JPAttrMap::getCode, Function.identity()));
  }

  /**
   * Кодовое имя класса
   *
   * @return Кодовое имя класса
   */
  public String getCode() {
    return code;
  }

  /**
   * Кодовое имя хранилища
   *
   * @return Кодовое имя хранилища
   */
  public String getStorage() {
    return storage;
  }

  /**
   * Мап на БД
   *
   * @return Мап на БД
   */
  public String getMap() {
    return map;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    JPClassMapBean that = (JPClassMapBean) o;
    return Objects.equals(code, that.code) &&
        Objects.equals(storage, that.storage) &&
        Objects.equals(map, that.map);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, storage, map);
  }

  /**
   * Маппинг атрибутов
   *
   * @return Маппинг атрибутов
   */
  public Collection<JPAttrMap> getAttrs() {
    return attrs.values();
  }

  /**
   * Маппинг атрибутов
   *
   * @return Маппинг атрибутов
   */
  public JPAttrMap getAttr(String code) {
    return attrs.get(code);
  }

  /**
   * Признак неизменяемой привязки к хранилищу
   *
   * @return Да/Нет
   */
  @Override
  public boolean isImmutable() {
    return immutable;
  }

  /**
   * Построитель JPClassMap
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  @Override
  public String toString() {
    return "JPClassMap{" +
        "code='" + code + '\'' +
        ", storage='" + storage + '\'' +
        ", map='" + map + '\'' +
        ", attrs=" + attrs +
        ", immutable = " + immutable +
        '}';
  }

  /**
   * Построитель JPClassMap
   */
  public static final class Builder {
    private String code;
    private String storage;
    private String map;
    private Collection<JPAttrMap> attrs;
    private boolean immutable = true;

    private Builder() {
    }

    /**
     * Создаем метаописание
     *
     * @return Метаописание
     */
    public JPClassMapBean build() {
      return new JPClassMapBean(code, storage, map, attrs, immutable);
    }

    /**
     * Мап на БД
     *
     * @param map Мап на БД
     * @return Builder
     */
    public Builder map(String map) {
      this.map = map;
      return this;
    }


    /**
     * Кодовое имя атрибута
     *
     * @param code Кодовое имя атрибута
     * @return Builder
     */
    public Builder code(String code) {
      this.code = code;
      return this;
    }

    /**
     * Кодовое имя хранилища
     *
     * @param storage Кодовое имя хранилища
     * @return Builder
     */
    public Builder storage(String storage) {
      this.storage = storage;
      return this;
    }

    /**
     * Маппинг атрибутов
     *
     * @param attrs Маппинг атрибутов
     * @return Builder
     */
    public Builder attrs(Collection<JPAttrMap> attrs) {
      this.attrs = attrs;
      return this;
    }

    /**
     * Признак неизменяемости
     *
     * @param immutable Признак неизменяемости
     * @return Builder
     */
    public Builder immutable(boolean immutable) {
      this.immutable = immutable;
      return this;
    }
  }
}