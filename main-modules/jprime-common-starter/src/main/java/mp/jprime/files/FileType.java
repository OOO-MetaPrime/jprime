package mp.jprime.files;

import org.apache.commons.lang3.StringUtils;

/**
 * Типы файлов
 */
public enum FileType {
  TXT("txt"),
  XML("XML"),
  XLSX("xlsx"),
  XLS("xls"),
  CSV("csv"),
  DOC("doc"),
  DOCX("docx"),
  PDF("pdf"),
  JRXML("jrxml"),
  HTML("html");

  private String ext;

  FileType(String ext) {
    this.ext = ext;
  }

  public String getExt() {
    return ext;
  }

  @Override
  public String toString() {
    return "FileType{" +
        "ext='" + ext + '\'' +
        '}';
  }

  /**
   * Возвращает тип файла
   *
   * @param code Код
   * @return тип файла
   */
  public static FileType getType(String code) {
    if (code == null) {
      return null;
    }
    for (FileType v : FileType.values()) {
      if (v.ext.equalsIgnoreCase(code)) {
        return v;
      }
    }
    return null;
  }

  /**
   * Возвращает тип файла
   *
   * @param name Имя файла
   * @return тип файла
   */
  public static FileType getTypeFromName(String name) {
    String inExt = name != null ? StringUtils.substringAfterLast(name, ".") : null;
    return inExt != null ? FileType.getType(inExt) : null;
  }
}
