package mp.jprime.functions.groups;

import mp.jprime.functions.JPFunction;
import mp.jprime.functions.JPFunctionGroup;
import mp.jprime.functions.base.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Группа функций для работы с целыми числами
 * <p>
 * код группы - integerFuncs
 * <p>
 * sum - сумма чисел
 */
@Service
public class JPFunctionIntegerGroup implements JPFunctionGroup {
  private final Map<String, JPFunction<?>> CACHE = new HashMap<>();

  public JPFunctionIntegerGroup(
      @Autowired JPSumIntegerFunction sum
  ) {
    CACHE.put("sum", sum);
  }

  @Override
  public String getGroupCode() {
    return "integerFuncs";
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
