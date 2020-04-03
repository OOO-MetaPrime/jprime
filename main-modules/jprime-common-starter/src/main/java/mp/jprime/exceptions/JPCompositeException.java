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
public class JPCompositeException extends JPRuntimeException implements CompositeException<JPRuntimeException> {
  private final List<JPRuntimeException> data = new ArrayList<>();
  private final Collection<JPRuntimeException> umData = Collections.unmodifiableList(data);

  /**
   * Конструкор
   */
  public JPCompositeException() {
  }

  /**
   * Конструктор
   *
   * @param data Данные ошибок
   */
  public JPCompositeException(Collection<JPRuntimeException> data) {
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
  public JPCompositeException addException(JPRuntimeException eData) {
    if (eData != null) {
      data.add(eData);
    }
    return this;
  }

  /**
   * Данные ошибок
   *
   * @return Данные ошибок
   */
  public Collection<JPRuntimeException> getExceptions() {
    return umData;
  }

  @Override
  public String getMessage() {
    return data.stream().map(JPRuntimeException::getMessage).collect(Collectors.joining(", "));
  }

  /**
   * Добавляем текущее описание ошибки в список
   *
   * @param list Список
   */
  public void fillData(Collection<JPRuntimeException> list) {
    list.addAll(data);
  }
}
