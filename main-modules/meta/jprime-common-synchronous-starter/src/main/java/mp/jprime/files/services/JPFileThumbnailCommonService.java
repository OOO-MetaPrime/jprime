package mp.jprime.files.services;

import mp.jprime.files.FileImageType;
import mp.jprime.files.JPFileInfo;
import mp.jprime.files.JPFileThumbnail;
import mp.jprime.files.JPFileThumbnailService;
import mp.jprime.files.beans.FileInfo;
import mp.jprime.repositories.JPFileStorage;
import mp.jprime.repositories.RepositoryGlobalStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Создание миниатюр по файлу
 */
@Service
public class JPFileThumbnailCommonService implements JPFileThumbnailService {
  private static final Logger LOG = LoggerFactory.getLogger(JPFileThumbnailCommonService.class);

  private JPFileThumbnail defaultThumbnail;
  private final Map<Integer, JPFileThumbnail> thumbnails = new ConcurrentHashMap<>();

  private JPFileThumbnail getDefaultThumbnail(Resource defaultImage) throws IOException {
    return JPFileThumbnail.ofByteArray("thumbnail." + FileImageType.JPG.getExt(), defaultImage.getContentAsByteArray());
  }

  @Value("classpath:thumbs/default.jpg")
  private void setDefaultThumbnail(Resource defaultImage) throws IOException {
    defaultThumbnail = getDefaultThumbnail(defaultImage);
  }

  @Value("classpath:thumbs/default_24.jpg")
  private void setDefaultThumbnail_24(Resource defaultImage) throws IOException {
    thumbnails.put(24, getDefaultThumbnail(defaultImage));
  }

  @Value("classpath:thumbs/default_48.jpg")
  private void setDefaultThumbnail_48(Resource defaultImage) throws IOException {
    thumbnails.put(48, getDefaultThumbnail(defaultImage));
  }

  /**
   * Описание всех хранилищ системы
   */
  private RepositoryGlobalStorage repositoryStorage;

  @Autowired
  private void setRepositoryStorage(RepositoryGlobalStorage repositoryStorage) {
    this.repositoryStorage = repositoryStorage;
  }

  @Override
  public JPFileThumbnail toThumbnail(JPFileInfo info, int width) {
    String fileTitle = info != null ? info.getFileTitle() : null;
    FileImageType imageType = FileImageType.getTypeFromName(fileTitle);
    if (imageType == null || info == null) {
      return getDefaultThumbnail(width);
    }
    JPFileStorage storage = (JPFileStorage) repositoryStorage.getStorage(info.getStorageCode());
    if (storage == null) {
      return getDefaultThumbnail(width);
    }
    String path = info.getStorageFilePath();
    String fileName = info.getStorageFileName();
    FileInfo fileInfo = storage.getInfo(path, fileName);
    if (fileInfo == null) {
      return getDefaultThumbnail(width);
    }
    return toThumbnail(storage.read(path, fileName), imageType, width);
  }

  @Override
  public JPFileThumbnail toThumbnail(InputStream is, FileImageType imageType, int width) {
    try {
      return JPFileThumbnail.of(
          true,
          "thumbnail." + imageType.getExt(),
          toInputStream(is, imageType, width)
      );
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      return getDefaultThumbnail(width);
    }
  }

  private InputStream toInputStream(InputStream is, FileImageType imageType, int width) throws IOException {
    BufferedImage scaleImage = scaleImage(ImageIO.read(is), width);

    ByteArrayOutputStream os = new ByteArrayOutputStream();
    ImageIO.write(scaleImage, imageType.getExt(), os);
    return new ByteArrayInputStream(os.toByteArray());
  }

  private BufferedImage scaleImage(BufferedImage src, int targetWidth) {
    float currentWidth = src.getWidth();
    float currentHeight = src.getHeight();

    // Рассчитываем высоту пропорционально
    int targetHeight = (int) (currentHeight / (currentWidth / targetWidth));

    BufferedImage result = new BufferedImage(
        targetWidth,
        targetHeight,
        src.getTransparency() == Transparency.OPAQUE ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB
    );

    Graphics2D resultGraphics = result.createGraphics();
    resultGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
    resultGraphics.drawImage(src, 0, 0, targetWidth, targetHeight, null);
    resultGraphics.dispose();

    return result;
  }

  private JPFileThumbnail getDefaultThumbnail(int width) {
    try {
      JPFileThumbnail thumbnail = thumbnails.get(width);
      if (thumbnail != null) {
        return thumbnail;
      }
      try {
        return JPFileThumbnail.of(
            false,
            defaultThumbnail.getFileName(),
            toInputStream(defaultThumbnail.getInputStream(), FileImageType.JPG, width)
        );
      } catch (Exception e) {
        LOG.error(e.getMessage(), e);
        return defaultThumbnail;
      }
    } catch (Exception io) {
      return null;
    }
  }
}
