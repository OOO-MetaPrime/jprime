package mp.jprime.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Параметры для массового чека утилит
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class JPUtilBatchCheckInParams {
  public static class CheckIds {
    private String objectClassCode;
    private Collection<String> objectIds;

    public String getObjectClassCode() {
      return objectClassCode;
    }

    public void setObjectClassCode(String objectClassCode) {
      this.objectClassCode = objectClassCode;
    }

    public Collection<String> getObjectIds() {
      return objectIds;
    }

    public void setObjectIds(Collection<String> objectIds) {
      this.objectIds = objectIds;
    }
  }

  /**
   * кодовое имя метакласса корневого объекта
   */
  private String rootObjectClassCode;
  /**
   * идентификатор корневого объекта
   */
  private String rootObjectId;
  /**
   * Идентификаторы проверяемых объектов
   */
  private Collection<CheckIds> ids = new ArrayList<>();
  /**
   * Коды проверяемых утилит
   */
  private Collection<String> utils = new ArrayList<>();

  public String getRootObjectClassCode() {
    return rootObjectClassCode;
  }

  public void setRootObjectClassCode(String rootObjectClassCode) {
    this.rootObjectClassCode = rootObjectClassCode;
  }

  public String getRootObjectId() {
    return rootObjectId;
  }

  public void setRootObjectId(String rootObjectId) {
    this.rootObjectId = rootObjectId;
  }

  public Collection<CheckIds> getIds() {
    return ids;
  }

  public void setIds(Collection<CheckIds> ids) {
    this.ids = ids;
  }

  public Collection<String> getUtils() {
    return utils;
  }

  public void setUtils(Collection<String> utils) {
    this.utils = utils;
  }
}
