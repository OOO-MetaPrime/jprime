package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonObjectLinkedData {
  private Collection<JsonObjectData> create = new ArrayList<>();
  private Collection<JsonObjectData> update = new ArrayList<>();
  private Collection<JsonObjectData> delete = new ArrayList<>();

  public Collection<JsonObjectData> getCreate() {
    return create;
  }

  public void setCreate(Collection<JsonObjectData> create) {
    this.create = create;
  }

  public Collection<JsonObjectData> getUpdate() {
    return update;
  }

  public void setUpdate(Collection<JsonObjectData> update) {
    this.update = update;
  }

  public Collection<JsonObjectData> getDelete() {
    return delete;
  }

  public void setDelete(Collection<JsonObjectData> delete) {
    this.delete = delete;
  }

  public JsonObjectLinkedData() {

  }

  public JsonObjectLinkedData(Collection<JsonObjectData> create, Collection<JsonObjectData> update, Collection<JsonObjectData> delete) {
    this.create = create;
    this.update = update;
    this.delete = delete;
  }
}
