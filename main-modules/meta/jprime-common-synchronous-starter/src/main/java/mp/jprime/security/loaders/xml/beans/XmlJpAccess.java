package mp.jprime.security.loaders.xml.beans;

public class XmlJpAccess {
  private boolean read;
  private boolean create;
  private boolean update;
  private boolean delete;
  private String role;

  public boolean isRead() {
    return read;
  }

  public void setRead(boolean read) {
    this.read = read;
  }

  public boolean isCreate() {
    return create;
  }

  public void setCreate(boolean create) {
    this.create = create;
  }

  public boolean isUpdate() {
    return update;
  }

  public void setUpdate(boolean update) {
    this.update = update;
  }

  public boolean isDelete() {
    return delete;
  }

  public void setDelete(boolean delete) {
    this.delete = delete;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  @Override
  public String toString() {
    return "XmlJpAttrMap{" +
        "role='" + role + '\'' +
        ", read='" + read + '\'' +
        ", create='" + create + '\'' +
        ", update='" + update + '\'' +
        ", delete='" + delete + '\'' +
        '}';
  }
}
