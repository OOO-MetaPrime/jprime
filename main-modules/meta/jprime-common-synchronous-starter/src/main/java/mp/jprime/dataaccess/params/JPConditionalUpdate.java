package mp.jprime.dataaccess.params;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.security.AuthInfo;

import java.util.Map;

/**
 * Запрос обновления по условию
 */
public class JPConditionalUpdate extends JPSave {
  private final String jpClass;
  private final Filter where;
  private final boolean autoChangeDate;

  private JPConditionalUpdate(Map<String, Object> data, Source source, AuthInfo auth, String jpClass,
                              Filter where, boolean autoChangeDate, Map<String, String> props) {
    super(data, null, source, auth, props);
    this.jpClass = jpClass;
    this.where = where;
    this.autoChangeDate = autoChangeDate;
  }

  /**
   * Кодовое имя класса
   *
   * @return Кодовое имя класса
   */
  public String getJpClass() {
    return jpClass;
  }

  /**
   * Устанавливает ли дату изменения
   *
   * @return Да/Нет
   */
  public boolean isAutoChangeDate() {
    return autoChangeDate;
  }

  /**
   * Условия
   *
   * @return Условия
   */
  public Filter getWhere() {
    return where;
  }

  /**
   * Построитель JPUpdate
   *
   * @param jpClass Кодовое имя метаописания класса
   * @param where   Условия
   * @return Builder
   */
  public static JPConditionalUpdate.Builder update(String jpClass, Filter where) {
    return new JPConditionalUpdate.Builder(jpClass, where);
  }

  /**
   * Построитель JPUpdate
   */
  public static final class Builder extends JPSave.Builder<Builder> {
    private final String jpClass;
    private Filter where;
    private boolean autoChangeDate = true;

    private Builder(String jpClass, Filter where) {
      this.jpClass = jpClass;
      this.where = where;
    }

    /**
     * Создаем JPUpdate
     *
     * @return JPUpdate
     */
    @Override
    public JPConditionalUpdate build() {
      return new JPConditionalUpdate(data, source, auth, jpClass, where, autoChangeDate, props);
    }

    /**
     * Возвращает кодовое имя класса
     *
     * @return Кодовое имя класса
     */
    @Override
    public String getJpClass() {
      return this.jpClass;
    }

    /**
     * Условие выборки
     *
     * @param where Условие выборки
     * @return Builder
     */
    public Builder andWhere(Filter where) {
      if (where == null) {
        return this;
      }
      if (this.where == null) {
        this.where = where;
      } else {
        this.where = Filter.and(this.where, where);
      }
      return this;
    }

    /**
     * Условие выборки
     *
     * @param where Условие выборки
     * @return Builder
     */
    public Builder orWhere(Filter where) {
      if (where == null) {
        return this;
      }
      if (this.where == null) {
        this.where = where;
      } else {
        this.where = Filter.or(this.where, where);
      }
      return this;
    }

    /**
     * Устанавливает ли дату изменения (по умолчанию {@code true})
     *
     * @param autoChangeDate Да/Нет
     * @return Builder
     */
    public Builder autoChangeDate(boolean autoChangeDate) {
      this.autoChangeDate = autoChangeDate;
      return this;
    }
  }
}
