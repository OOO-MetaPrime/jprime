package mp.jprime.dataaccess.handlers;

/**
 * Заполнение {@link JPClassHandlerStorage}
 */
public interface JPClassHandlerStorageAware {
  /**
   * Устанавливает   {@link JPClassHandlerStorage}
   *
   * @param handlerStorage {@link JPClassHandlerStorage}
   */
  void setJPClassHandlerStorage(JPClassHandlerStorage handlerStorage);
}
