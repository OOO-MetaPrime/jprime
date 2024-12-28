package mp.jprime.json.db.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mp.jprime.common.JPOrder;
import mp.jprime.common.JPOrderDirection;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonDbJPOrder {
  private String asc;
  private String desc;

  public JsonDbJPOrder() {

  }

  public String getAsc() {
    return asc;
  }

  public String getDesc() {
    return desc;
  }

  public void setAsc(String asc) {
    this.asc = asc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public static JsonDbJPOrder asc(String asc) {
    JsonDbJPOrder result = new JsonDbJPOrder();
    result.setAsc(asc);
    return result;
  }

  public static JsonDbJPOrder desc(String desc) {
    JsonDbJPOrder result = new JsonDbJPOrder();
    result.setDesc(desc);
    return result;
  }

  public static JPOrder toJPOrder(JsonDbJPOrder json) {
    if (json == null) {
      return null;
    }
    String asc = json.getAsc();
    String desc = json.getDesc();
    if (asc != null) {
      return JPOrder.asc(asc);
    }
    if (desc != null) {
      return JPOrder.desc(desc);
    }
    return null;
  }

  public static JsonDbJPOrder toJson(JPOrder bean) {
    if (bean == null) {
      return null;
    }
    String attr = bean.getAttr();
    JPOrderDirection direction = bean.getOrder();
    if (JPOrderDirection.ASC == direction) {
      return JsonDbJPOrder.asc(attr);
    }
    if (JPOrderDirection.DESC == direction) {
      return JsonDbJPOrder.desc(attr);
    }
    return null;
  }
}
