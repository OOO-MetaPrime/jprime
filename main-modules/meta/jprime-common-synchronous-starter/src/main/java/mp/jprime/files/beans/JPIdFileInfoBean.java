package mp.jprime.files.beans;

import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.files.JPIdFileInfo;

import java.time.LocalDateTime;

/**
 * Информация о файле, связанном с объектом мета-класса
 */
public final class JPIdFileInfoBean extends JPFileInfoBase implements JPIdFileInfo {
  private final JPId jpId;

  private JPIdFileInfoBean(String fileCode, String storageCode, String storageFilePath, String storageFileName, String fileTitle, String fileExt, Long fileSize, LocalDateTime fileDate, JPId jpId) {
    super(fileCode, storageCode, storageFilePath, storageFileName, fileTitle, fileExt, fileSize, fileDate);
    this.jpId = jpId;
  }

  /**
   * Идентификатор объекта, к которому относится файл
   */
  @Override
  public JPId getJPId() {
    return jpId;
  }

  /**
   * Построитель {@link JPIdFileInfoBean}
   *
   * @param jpId Идентификатор объекта, к которому относится файл
   */
  public static Builder newBuilder(JPId jpId) {
    return new Builder(jpId);
  }

  /**
   * Построитель {@link JPIdFileInfoBean}
   */
  public final static class Builder extends JPFileInfoBase.Builder<JPIdFileInfoBean.Builder> {
    private final JPId jpId;

    private Builder(JPId jpId) {
      this.jpId = jpId;
    }

    /**
     * Построить {@link JPIdFileInfoBean}
     *
     * @return {@link JPIdFileInfoBean}
     */
    public JPIdFileInfoBean build() {
      return new JPIdFileInfoBean(fileCode, storageCode, storageFilePath, storageFileName, fileTitle, fileExt, fileSize, fileDate, jpId);
    }
  }
}
