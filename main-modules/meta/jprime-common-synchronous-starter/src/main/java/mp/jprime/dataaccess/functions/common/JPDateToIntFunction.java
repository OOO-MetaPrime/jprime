package mp.jprime.dataaccess.functions.common;

import mp.jprime.dataaccess.functions.JPDataBaseFunction;
import mp.jprime.dataaccess.functions.JPDataFunctionParams;
import mp.jprime.dataaccess.functions.JPDataFunctionResult;
import mp.jprime.dataaccess.functions.beans.JPDataFunctionResultBean;
import mp.jprime.parsers.ParserService;
import mp.jprime.security.AuthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.function.Function;

/**
 * Функция преобразование даты в целое число
 */
@Service
public final class JPDateToIntFunction extends JPDataBaseFunction<Integer> {
  /**
   * Кодовое имя функции
   */
  private static final String CODE = "dateToInt";

  /**
   * Шаблоны
   */
  public interface Template {
    // Возраст на текущую дату
    String AGE = "age";
  }

  /**
   * Аргументы
   */
  private interface Arg {
    // поле с датой
    String DATE = "date";
  }

  /**
   * Шаблоны для вызова функции
   */
  private static final Collection<String> TEMPLATES = Set.of(
      Template.AGE
  );

  /**
   * Кодовые имена аргументов функции
   */
  private static final List<String> ARG_CODES = Collections.singletonList(Arg.DATE);

  private final ParserService parserService;

  private JPDateToIntFunction(@Autowired ParserService parserService) {
    this.parserService = parserService;
  }

  @Override
  public String getCode() {
    return CODE;
  }

  @Override
  public Collection<String> getTemplates() {
    return TEMPLATES;
  }

  @Override
  protected List<String> getArgCodes() {
    return ARG_CODES;
  }

  @Override
  protected Map<JPDataFunctionParams, JPDataFunctionResult<Integer>> compute(Collection<JPDataFunctionParams> args, AuthInfo auth) {
    Map<JPDataFunctionParams, JPDataFunctionResult<Integer>> result = new HashMap<>(args.size());
    args.forEach(x -> {
      LocalDate date = parserService.parseTo(LocalDate.class, x.getArgs().get(0));
      Function<LocalDate, Integer> func = date != null ? TEMPLATE_FUNC_LIST.get(x.getTemplate()) : null;

      Integer value = null;
      if (func != null) {
        value = func.apply(date);
      }
      result.put(x, JPDataFunctionResultBean.of(value));
    });
    return result;
  }

  // Функции трансформации согласно шаблона
  private static final Map<String, Function<LocalDate, Integer>> TEMPLATE_FUNC_LIST = Map.of(
      Template.AGE, date -> {
        // возраст
        return date == null ? null : Period.between(date, LocalDate.now()).getYears();
      }
  );
}
