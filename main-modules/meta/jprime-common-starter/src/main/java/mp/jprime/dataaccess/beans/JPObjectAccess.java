package mp.jprime.dataaccess.beans;

/**
 * Идентификатор объекта
 */
public class JPObjectAccess {
  private final String jpClass; // Кодовое имя метаописания класса
  private final Object id;
  private final boolean read;
  private final boolean create;
  private final boolean update;
  private final boolean delete;

  private JPObjectAccess(String jpClass, Object id, boolean read, boolean create, boolean update, boolean delete) {
    this.jpClass = jpClass;
    this.id = id;
    this.read = read;
    this.create = create;
    this.update = update;
    this.delete = delete;
  }

  /**
   * Кодовое имя метаописания класса
   *
   * @return Кодовое имя метаописания класса
   */
  public String getJpClass() {
    return jpClass;
  }

  /**
   * Идентификатор объекта
   *
   * @return Идентификатор объекта
   */
  public Comparable getId() {
    return (Comparable) id;
  }

  /**
   * Доступ на чтение
   *
   * @return Доступ на чтение
   */
  public boolean isRead() {
    return read;
  }

  /**
   * Доступ на создание
   *
   * @return Доступ на создание
   */
  public boolean isCreate() {
    return create;
  }

  /**
   * Доступ на изменеие
   *
   * @return Доступ на изменение
   */
  public boolean isUpdate() {
    return update;
  }

  /**
   * Доступ на удаление
   *
   * @return Доступ на удаление
   */
  public boolean isDelete() {
    return delete;
  }

  /**
   * Построитель JPObjectAccess
   *
   * @return Builder
   */
  public static JPObjectAccess.Builder newBuilder() {
    return new JPObjectAccess.Builder();
  }


  public static final class Builder {
    private String jpClass; // Кодовое имя метаописания класса
    private Object id;
    private boolean read = false;
    private boolean create = false;
    private boolean update = false;
    private boolean delete = false;

    public Builder jpClass(String jpClass) {
      this.jpClass = jpClass;
      return this;
    }

    public Builder id(Object id) {
      this.id = id;
      return this;
    }

    public Builder read(boolean read) {
      this.read = read;
      return this;
    }

    public Builder create(boolean create) {
      this.create = create;
      return this;
    }

    public Builder update(boolean update) {
      this.update = update;
      return this;
    }

    public Builder delete(boolean delete) {
      this.delete = delete;
      return this;
    }

    public JPObjectAccess build() {
      return new JPObjectAccess(jpClass, id, read, create, update, delete);
    }
  }
}
