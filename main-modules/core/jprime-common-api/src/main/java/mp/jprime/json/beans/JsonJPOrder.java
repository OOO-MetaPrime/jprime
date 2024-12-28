package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mp.jprime.common.JPOrder;
import mp.jprime.common.JPOrderDirection;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonJPOrder {
  private String asc;
  private String desc;

  public JsonJPOrder() {

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

  public static JsonJPOrder asc(String asc) {
    JsonJPOrder result = new JsonJPOrder();
    result.setAsc(asc);
    return result;
  }

  public static JsonJPOrder desc(String desc) {
    JsonJPOrder result = new JsonJPOrder();
    result.setDesc(desc);
    return result;
  }

  public static JPOrder toJPOrder(JsonJPOrder json) {
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

  public static JsonJPOrder toJson(JPOrder bean) {
    if (bean == null) {
      return null;
    }
    String attr = bean.getAttr();
    JPOrderDirection direction = bean.getOrder();
    if (JPOrderDirection.ASC == direction) {
      return JsonJPOrder.asc(attr);
    }
    if (JPOrderDirection.DESC == direction) {
      return JsonJPOrder.desc(attr);
    }
    return null;
  }
}
