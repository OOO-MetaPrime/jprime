package mp.jprime.files;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Типы image файлов
 */
public enum FileImageType {
  BMP("bmp"),
  GIF("gif"),
  JPEG("jpeg"),
  JPG("jpg"),
  PNG("png");

  private static final Map<String, FileImageType> BY_CODE = new HashMap<>();

  static {
    for (FileImageType type : FileImageType.values()) {
      BY_CODE.put(type.getExt().toLowerCase(), type);
    }
  }

  private final String ext;

  FileImageType(String ext) {
    this.ext = ext;
  }

  public String getExt() {
    return ext;
  }

  @Override
  public String toString() {
    return "FileImageType{" +
        "ext='" + ext + '\'' +
        '}';
  }

  /**
   * Возвращает тип файла
   *
   * @param code Код
   * @return тип файла
   */
  public static FileImageType getType(String code) {
    return code == null ? null : BY_CODE.get(code.toLowerCase());
  }

  /**
   * Возвращает тип файла
   *
   * @param name Имя файла
   * @return тип файла
   */
  public static FileImageType getTypeFromName(String name) {
    String inExt = name != null ? StringUtils.substringAfterLast(name, ".") : null;
    return inExt != null ? FileImageType.getType(inExt) : null;
  }
}
