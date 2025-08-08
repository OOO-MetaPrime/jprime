package mp.jprime.files.beans;

import mp.jprime.files.JPFileCommonInfo;

import java.time.LocalDateTime;

/**
 * Информация о файле, связанном с объектом мета-класса
 */
public final class JPFileCommonInfoBean extends JPFileInfoBase<String> implements JPFileCommonInfo {
  private JPFileCommonInfoBean(String fileCode, String storageCode, String storageFilePath, String storageFileName, String fileTitle, String fileExt, Long fileSize, LocalDateTime fileDate) {
    super(fileCode, storageCode, storageFilePath, storageFileName, fileTitle, fileExt, fileSize, fileDate);
  }

  /**
   * Построитель {@link JPFileCommonInfoBean}
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель {@link JPFileCommonInfoBean}
   */
  public final static class Builder extends JPFileInfoBase.Builder<String, JPFileCommonInfoBean.Builder> {
    /**
     * Построить {@link JPFileCommonInfoBean}
     *
     * @return {@link JPFileCommonInfoBean}
     */
    public JPFileCommonInfoBean build() {
      return new JPFileCommonInfoBean(fileCode, storageCode, storageFilePath, storageFileName, fileTitle, fileExt, fileSize, fileDate);
    }
  }
}
