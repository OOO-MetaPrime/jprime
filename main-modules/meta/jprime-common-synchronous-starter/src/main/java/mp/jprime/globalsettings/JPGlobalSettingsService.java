package mp.jprime.globalsettings;

import java.util.Collection;

/**
 * Сервис кеширования настроек системы
 */
public interface JPGlobalSettingsService {

  /**
   * Возвращает описание настройки по коду настройки
   *
   * @param code код настройки
   *
   * @return описание настройки
   */
  JPGlobalProperty getProperty(String code);

  /**
   * Возвращает значение настройки по коду настройки
   *
   * @param code код настройки
   *
   * @return значение настройки
   */
  <T> T getValue(String code);

  /**
   * Возвращает список кодов настроек
   *
   * @return Список кодов настроек
   */
  Collection<String> getSettingsCodeList();
}