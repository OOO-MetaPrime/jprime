package mp.jprime.files;

import java.io.InputStream;

/**
 * Создание миниатюр по файлу
 */
public interface JPFileThumbnailService {
  /**
   * Формирование миниатюры
   *
   * @param info  Данные оригинального файла
   * @param width Ширина
   * @return Миниатюра
   */
  JPFileThumbnail toThumbnail(JPFileInfo info, int width);

  /**
   * Формирование миниатюры
   *
   * @param is        Данные оригинального файла
   * @param imageType FileImageType
   * @param width     Ширина
   * @return Миниатюра
   */
  JPFileThumbnail toThumbnail(InputStream is, FileImageType imageType, int width);
}
