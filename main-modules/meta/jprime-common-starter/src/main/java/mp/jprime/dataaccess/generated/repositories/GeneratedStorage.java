package mp.jprime.dataaccess.generated.repositories;

import mp.jprime.repositories.JPObjectStorage;

/**
 * Хранилище типа Управляемое
 */
public abstract class GeneratedStorage extends JPObjectStorage {
  /**
   * Код хранилища
   */
  public static final String CODE = "generated";
  /**
   * Конструктор
   *
   * @param code  Код хранилища
   * @param title Название хранилища
   */
  public GeneratedStorage(String code, String title) {
    super(code, title);
  }
}