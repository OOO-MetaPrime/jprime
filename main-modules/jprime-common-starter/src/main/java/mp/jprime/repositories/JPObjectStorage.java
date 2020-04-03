package mp.jprime.repositories;

/**
 * Описание типового хранилища JPObject
 */
public abstract class JPObjectStorage extends JPBaseStorage {
  /**
   * Конструктор
   *
   * @param code  Код хранилища
   * @param title Название хранилища
   */
  protected JPObjectStorage(String code, String title) {
    super(code, title);
  }
}