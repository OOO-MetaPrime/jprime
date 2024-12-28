package mp.jprime.nsi;

import org.springframework.beans.factory.Aware;

/**
 * Заполнение {@link JpNsiStorage}
 */
public interface JpNsiStorageAware extends Aware {
  /**
   * Устанавливает  {@link JpNsiStorage}
   *
   * @param nsiStorage {@link JpNsiStorage}
   */
  void setJpNsiStorage(JpNsiStorage nsiStorage);
}
