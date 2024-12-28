package mp.jprime.files;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Типы файлов
 */
public enum FileType {
  CSV("csv"),
  DOC("doc"),
  DOCX("docx"),
  FO("fo"),
  HTML("html"),
  JRXML("jrxml"),
  PDF("pdf"),
  TXT("txt"),
  XLS("xls"),
  XLSX("xlsx"),
  XML("xml"),
  ZIP("zip");

  private static final Map<String, FileType> BY_CODE = new HashMap<>();

  static {
    for (FileType type : FileType.values()) {
      BY_CODE.put(type.getExt().toLowerCase(), type);
    }
  }

  private final String ext;

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
    return code == null ? null : BY_CODE.get(code.toLowerCase());
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
