package mp.jprime.functions.groups;

import mp.jprime.functions.JPFunction;
import mp.jprime.functions.JPFunctionGroup;
import mp.jprime.functions.base.JPAddDayLocalDateFunction;
import mp.jprime.functions.base.JPCurLocalDateFunction;
import mp.jprime.functions.base.JPMaxLocalDateFunction;
import mp.jprime.functions.base.JPMinLocalDateFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Группа функций для работы с датами
 * <p>
 * код группы - localDateFuncs
 * <p>
 * addDay - добавление к указанной дате N дней
 * curDate - текущая дата
 * max - максимальная из указанных дат
 * min - минимальная из указанных дат
 */
@Service
public class JPFunctionLocalDateGroup implements JPFunctionGroup {
  private final Map<String, JPFunction<?>> CACHE = new HashMap<>();

  public JPFunctionLocalDateGroup(
      @Autowired JPAddDayLocalDateFunction addDay,
      @Autowired JPCurLocalDateFunction curDate,
      @Autowired JPMaxLocalDateFunction maxDate,
      @Autowired JPMinLocalDateFunction minDate
  ) {
    CACHE.put("addDay", addDay);
    CACHE.put("curDate", curDate);
    CACHE.put("max", maxDate);
    CACHE.put("min", minDate);
  }

  @Override
  public String getGroupCode() {
    return "localDateFuncs";
  }

  @Override
  public Collection<String> getFunctionCodes() {
    return CACHE.keySet();
  }

  @Override
  public JPFunction<?> getFunction(String code) {
    return CACHE.get(code);
  }
}
