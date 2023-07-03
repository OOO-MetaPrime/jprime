package mp.jprime.functions.base;

import mp.jprime.functions.JPFunction;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Текущая дата
 * <p>
 * Параметры:
 * - нет
 */
@Service
public class JPCurLocalDateFunction implements JPFunction<LocalDate> {
  @Override
  public LocalDate eval(Object... params) {
    return LocalDate.now();
  }
}
