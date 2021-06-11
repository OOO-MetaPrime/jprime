package mp.jprime.dataaccess.params;

import mp.jprime.dataaccess.Source;
import mp.jprime.meta.JPClass;
import mp.jprime.security.AuthInfo;

import java.util.Collection;
import java.util.Map;

/**
 * Запрос множественного создания
 */
public class JPBatchCreate extends JPBatchSave {
  private final String jpClass;

  public JPBatchCreate(Collection<Map<String, Object>> data, Source source, AuthInfo auth, String jpClass) {
    super(data, source, auth);
    this.jpClass = jpClass;
  }

  public String getJpClass() {
    return jpClass;
  }

  /**
   * Построитель JPCreate
   *
   * @param jpClass Кодовое имя класса
   * @return Builder
   */
  public static Builder create(String jpClass) {
    return new Builder(jpClass);
  }

  /**
   * Построитель JPCreate
   *
   * @param jpClass Мета класс
   * @return Builder
   */
  public static Builder create(JPClass jpClass) {
    return new Builder(jpClass == null ? null : jpClass.getCode());
  }

  /**
   * Построитель JPCreate
   */
  public static final class Builder extends JPBatchSave.Builder<Builder> {
    private final String jpClass;

    private Builder(String jpClass) {
      this.jpClass = jpClass;
    }

    /**
     * Создаем JPCreate
     *
     * @return JPCreate
     */
    @Override
    public JPBatchCreate build() {
      return new JPBatchCreate(allData, source, auth, jpClass);
    }

    /**
     * Возвращает кодовое имя класса
     *
     * @return Кодовое имя класса
     */
    @Override
    public String getJpClass() {
      return jpClass;
    }
  }
}
