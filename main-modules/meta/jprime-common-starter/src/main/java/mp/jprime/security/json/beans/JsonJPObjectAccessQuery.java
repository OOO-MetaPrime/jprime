package mp.jprime.security.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collection;

/**
 * Запрос массовой проверки доступа текущим пользователем к указанным объектам определенного класса
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonJPObjectAccessQuery {
  /**
   * Код класса
   */
  private String objectClassCode;
  /**
   * Идентификаторы объектов
   */
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
