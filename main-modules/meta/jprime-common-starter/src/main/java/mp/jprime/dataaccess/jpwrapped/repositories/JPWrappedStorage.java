package mp.jprime.dataaccess.jpwrapped.repositories;

import mp.jprime.repositories.JPObjectStorage;

/**
 * Хранилище типа Переопределенное
 */
public abstract class JPWrappedStorage extends JPObjectStorage {
  /**
   * Код хранилища
   */
  public static final String CODE = "jpwrapped";
  /**
   * Конструктор
   *
   * @param code  Код хранилища
   * @param title Название хранилища
   */
  public JPWrappedStorage(String code, String title) {
    super(code, title);
  }
}