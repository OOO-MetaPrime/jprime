package mp.jprime.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collection;

/**
 * Входящие параметры утилиты
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseJPUtilInParams implements JPUtilInParams {
  private String rootObjectClassCode;
  private String rootObjectId;
  private String objectClassCode;
  private Collection<String> objectIds;

  /**
   * кодовое имя метакласса корневого объекта
   *
   * @return кодовое имя метакласса корневого объекта
   */
  @Override
  public String getRootObjectClassCode() {
    return rootObjectClassCode;
  }

  /**
   * идентификатор корневого объекта
   *
   * @return идентификатор корневого объекта
   */
  @Override
  public String getRootObjectId() {
    return rootObjectId;
  }

  /**
   * кодовое имя метакласса объекта/ов
   *
   * @return кодовое имя метакласса объекта/ов
   */
  @Override
  public String getObjectClassCode() {
    return objectClassCode;
  }

  /**
   * идентификатор или идентификаторы объектов указанного класса
   *
   * @return идентификатор или идентификаторы объектов указанного класса
   */
  @Override
  public Collection<String> getObjectIds() {
    return objectIds;
  }

  public void setRootObjectClassCode(String rootObjectClassCode) {
    this.rootObjectClassCode = rootObjectClassCode;
  }

  public void setRootObjectId(String rootObjectId) {
    this.rootObjectId = rootObjectId;
  }

  public void setObjectClassCode(String objectClassCode) {
    this.objectClassCode = objectClassCode;
  }

  public void setObjectIds(Collection<String> objectIds) {
    this.objectIds = objectIds;
  }
}
