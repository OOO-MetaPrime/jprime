package mp.jprime.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Описание типового хранилища
 */
public abstract class JPBaseStorage implements JPStorage {
  public static final Logger LOG = LoggerFactory.getLogger(JPBaseStorage.class);
  /**
   * Кодовое имя
   */
  private String code;
  /**
   * Название
   */
  private String title;

  /**
   * Кодовое имя
   *
   * @param code Кодовое имя
   */
  protected JPBaseStorage(String code, String title) {
    this.code = code;
    this.title = title;
  }

  /**
   * Возвращает кодовое имя хранилища
   *
   * @return Кодовое имя
   */
  @Override
  public String getCode() {
    return code;
  }

  /**
   * Возвращает название хранилища
   *
   * @return Название
   */
  @Override
  public String getTitle() {
    return title;
  }
}