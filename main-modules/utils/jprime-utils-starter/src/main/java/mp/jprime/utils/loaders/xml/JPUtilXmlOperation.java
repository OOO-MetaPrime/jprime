package mp.jprime.utils.loaders.xml;

import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.security.AuthInfo;
import mp.jprime.utils.loaders.xml.beans.XmlJpOperation;
import org.springframework.web.server.ServerWebExchange;

import java.util.Map;

/**
 * Операция утилиты
 */
public interface JPUtilXmlOperation {
  /**
   * Код операции
   *
   * @return Код операции
   */
  String getCode();

  /**
   * Создает новую настройку
   *
   * @param xml XML
   * @return Новая настройка
   */
  Executor newOperation(XmlJpOperation xml);

  /**
   * Выполнение операции
   */
  interface Executor {
    /**
     * Выполнение
     *
     * @param cache       Кеш данных
     * @param paramValues Значения параметров
     * @param swe         ServerWebExchange
     * @param auth        AuthInfo
     */
    void execute(Map<String, JPObject> cache,
                 Map<String, Object> paramValues, ServerWebExchange swe, AuthInfo auth);
  }
}
