package mp.jprime.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Базовая реализация исходящих параметров утилиты
 */
abstract class BaseJPUtilOutParams<T> implements JPUtilOutParams<T> {
  private final String description;
  private final String qName;
  private final boolean changeData;


  BaseJPUtilOutParams(String description, String qName, boolean changeData) {
    this.description = description;
    this.qName = qName;
    this.changeData = changeData;
  }

  /**
   * Описание результата
   *
   * @return Описание результата
   */
  @Override
  public String getDescription() {
    return description;
  }

  /**
   * Полное кодовое имя описаний результата
   *
   * @return Полное кодовое имя описаний результата
   */
  @Override
  @JsonProperty("qName")
  public String getQName() {
    return qName;
  }

  /**
   * Признак изменения исходных данных
   *
   * @return Да/Нет
   */
  @Override
  public boolean isChangeData() {
    return changeData;
  }

  protected abstract static class Builder<T extends Builder> {
    protected String description;
    protected String qName;
    protected boolean changeData;

    protected Builder() {
    }

    abstract public BaseJPUtilOutParams build();

    public T description(String description) {
      this.description = description;
      return (T) this;
    }

    public T qName(String qName) {
      this.qName = qName;
      return (T) this;
    }

    public T changeData(boolean changeData) {
      this.changeData = changeData;
      return (T) this;
    }
  }
}
