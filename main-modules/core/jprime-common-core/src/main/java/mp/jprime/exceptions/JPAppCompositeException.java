package mp.jprime.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Агрегация всевозможных ошибок
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JPAppCompositeException extends JPAppRuntimeException implements CompositeException<JPAppRuntimeException> {
  private final List<JPAppRuntimeException> data = new ArrayList<>();
  private final Collection<JPAppRuntimeException> umData = Collections.unmodifiableList(data);

  /**
   * Конструкор
   */
  public JPAppCompositeException() {
  }

  /**
   * Конструктор
   *
   * @param data Данные ошибок
   */
  public JPAppCompositeException(Collection<JPAppRuntimeException> data) {
    if (data != null) {
      this.data.addAll(data);
    }
  }

  /**
   * Добавлеяет данные ошибок
   *
   * @param eData Данные ошибок
   * @return Данные ошибок
   */
  public JPAppCompositeException addException(JPAppRuntimeException eData) {
    if (eData != null) {
      data.add(eData);
    }
    return this;
  }

  /**
   * Добавлеяет данные ошибок
   *
   * @param eData Данные ошибок
   * @return Данные ошибок
   */
  public JPAppCompositeException addExceptions(Collection<JPAppRuntimeException> eData) {
    if (eData != null) {
      data.addAll(eData);
    }
    return this;
  }

  /**
   * Данные ошибок
   *
   * @return Данные ошибок
   */
  public Collection<JPAppRuntimeException> getExceptions() {
    return umData;
  }

  @Override
  public String getMessage() {
    return data.stream().map(JPAppRuntimeException::getMessage).collect(Collectors.joining(", "));
  }

  /**
   * Добавляем текущее описание ошибки в список
   *
   * @param list Список
   */
  public void fillData(Collection<JPAppRuntimeException> list) {
    list.addAll(data);
  }
}
