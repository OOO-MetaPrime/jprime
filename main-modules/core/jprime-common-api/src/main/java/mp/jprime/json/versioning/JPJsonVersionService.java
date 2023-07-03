package mp.jprime.json.versioning;

import mp.jprime.lang.JPJsonNode;

/**
 * Сервис по трансформации данных в формате json с учетом версии
 */
public interface JPJsonVersionService {
  /**
   * Трансформирует данные из json в бин текущей версии
   *
   * @param groupCode Код группы версий
   * @param value     json данные
   * @param <T>       Тип бина
   * @return Бин
   */
  <T> T toObject(String groupCode, JPJsonNode value);

  /**
   * Трансформирует данные из json в бин в бин последней версии
   *
   * @param groupCode Код группы версий
   * @param value     json данные
   * @param <T>       Тип бина
   * @return Бин
   */
  <T> T toLatestObject(String groupCode, JPJsonNode value);

  /**
   * Трансформирует данные из json в json версии
   *
   * @param groupCode Код группы версий
   * @param value     json данные
   * @return json
   */
  JPJsonVersion toVersion(String groupCode, JPJsonNode value);

  /**
   * Трансформирует данные из json в json последней версии
   *
   * @param groupCode Код группы версий
   * @param value     json данные
   * @return json
   */
  JPJsonVersion toLatestVersion(String groupCode, JPJsonNode value);

  /**
   * Трансформирует данные из json в json последней версии
   *
   * @param groupCode Код группы версий
   * @param value     json данные
   * @return json
   */
  JPJsonVersion toLatestVersion(String groupCode, JPJsonVersion value);

  /**
   * Трансформирует данные из json в бин версии
   *
   * @param groupCode Код группы версий
   * @param value     json данные
   * @return json
   */
  <T> JPJsonBeanVersion<T> toBeanVersion(String groupCode, JPJsonNode value);

  /**
   * Трансформирует данные из json в бин версии
   *
   * @param groupCode Код группы версий
   * @param value     json данные
   * @return json
   */
  <T> JPJsonBeanVersion<T> toBeanVersion(String groupCode, JPJsonVersion value);

  /**
   * Трансформирует данные из json в бин последней версии
   *
   * @param groupCode Код группы версий
   * @param value     json данные
   * @return json
   */
  <T> JPJsonBeanVersion<T> toLatestBeanVersion(String groupCode, JPJsonNode value);

  /**
   * Трансформирует данные из json в бин последней версии
   *
   * @param groupCode Код группы версий
   * @param value     json данные
   * @return json
   */
  <T> JPJsonBeanVersion<T> toLatestBeanVersion(String groupCode, JPJsonVersion value);
}
