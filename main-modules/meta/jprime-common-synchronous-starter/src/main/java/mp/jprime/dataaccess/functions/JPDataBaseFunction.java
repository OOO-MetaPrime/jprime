package mp.jprime.dataaccess.functions;

import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.security.AuthInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Общая логика функций
 */
public abstract class JPDataBaseFunction<V> implements JPDataFunction<V> {
  protected static final Logger LOG = LoggerFactory.getLogger(JPDataBaseFunction.class);

  protected static final String DEFAULT_RESULT = StringUtils.EMPTY;

  /**
   * Получает данные функции
   *
   * @param args аргументы функции
   * @param auth авторизация
   * @return Параметры - результат
   */
  @Override
  public Map<JPDataFunctionParams, JPDataFunctionResult<V>> eval(Collection<JPDataFunctionParams> args, AuthInfo auth) {
    if (args != null && !args.isEmpty()) {
      check(args);
      return compute(args, auth);
    } else {
      return Collections.emptyMap();
    }
  }

  private void check(Collection<JPDataFunctionParams> allArgs) {
    allArgs.forEach(x -> {
      Collection<Object> args = x.getArgs();
      if (args == null || args.size() != this.getArgCodes().size()) {
        throw new JPRuntimeException("Неверные настройки: количество значений аргументов не совпадает");
      }
    });
  }

  /**
   * Получение данных функцией
   *
   * @param args аргументы функции
   * @param auth авторизация
   * @return Параметры-результат
   */
  protected abstract Map<JPDataFunctionParams, JPDataFunctionResult<V>> compute(Collection<JPDataFunctionParams> args, AuthInfo auth);

  /**
   * @return список кодовых имен аргументов функции
   */
  protected abstract List<String> getArgCodes();

  /**
   * Возвращает кодовое имя аргумента функции по его индексу
   *
   * @param index индекс аргумента
   * @return Кодовое имя аргумента функции
   */
  @Override
  public String getArgCode(int index) {
    if (index < getArgCodes().size() && index >= 0) {
      return getArgCodes().get(index);
    } else {
      return null;
    }
  }
}
