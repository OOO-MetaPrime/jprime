package mp.jprime.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mp.jprime.json.beans.JsonJPObject;
import mp.jprime.utils.annotations.JPUtilResultType;

import java.util.Collection;

/**
 * Тип результата - список объектов
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JPUtilResultType(code = "jpObjectList")
public final class JPUtilJPObjectListOutParams extends BaseJPUtilOutParams<Collection<JsonJPObject>> {
  private final Collection<JsonJPObject> jpObjectList;


  private JPUtilJPObjectListOutParams(String description, String qName, boolean changeData, Collection<JsonJPObject> jpObjectList) {
    super(description, qName, changeData);
    this.jpObjectList = jpObjectList;
  }

  /**
   * Результата
   *
   * @return Результат
   */
  @Override
  public Collection<JsonJPObject> getResult() {
    return jpObjectList;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public final static class Builder extends BaseJPUtilOutParams.Builder<Builder> {
    private Collection<JsonJPObject> jpObjectList;

    private Builder() {
      super();
    }

    @Override
    public JPUtilJPObjectListOutParams build() {
      return new JPUtilJPObjectListOutParams(description, qName, changeData, jpObjectList);
    }

    public Builder jpObjectList(Collection<JsonJPObject> jpObjectList) {
      this.jpObjectList = jpObjectList;
      return this;
    }
  }
}
