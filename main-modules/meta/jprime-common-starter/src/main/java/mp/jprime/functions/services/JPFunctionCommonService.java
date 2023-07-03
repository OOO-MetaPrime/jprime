package mp.jprime.functions.services;

import mp.jprime.exceptions.JPNotFoundException;
import mp.jprime.functions.JPFunction;
import mp.jprime.functions.JPFunctionGroup;
import mp.jprime.functions.JPFunctionService;
import mp.jprime.lang.JPMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class JPFunctionCommonService implements JPFunctionService {
  private final Map<String, Map<String, JPFunction<?>>> CACHE = new HashMap<>();

  private JPFunctionCommonService(@Autowired(required = false) Collection<JPFunctionGroup> groups) {
    if (groups == null || groups.isEmpty()) {
      return;
    }
    for (JPFunctionGroup group : groups) {
      String groupCode = group.getGroupCode();
      Collection<String> funcs = group.getFunctionCodes();
      if (groupCode == null || funcs == null || funcs.isEmpty()) {
        continue;
      }
      Map<String, JPFunction<?>> funcsCache = CACHE.computeIfAbsent(groupCode, x -> new HashMap<>());
      funcs.forEach(x -> funcsCache.put(x, group.getFunction(x)));
    }
  }

  @Override
  public <T> T eval(String groupCode, String functionCode, Object... params) {
    Map<String, JPFunction<?>> funcsCache = CACHE.get(groupCode);
    JPFunction<?> func = funcsCache != null ? funcsCache.get(functionCode) : null;
    if (func == null) {
      throw new JPNotFoundException(groupCode + "." + functionCode + " not found", "Функция " + groupCode + "." + functionCode + " не найдена");
    }
    return (T) func.eval(params);
  }

  @Override
  public <T> T eval(String functionTemplate, JPMap data) {
    if (functionTemplate == null) {
      throw new JPNotFoundException("Function not found", "Функция не найдена");
    }
    String[] split = functionTemplate.split("\\$");
    // из первой подстроки имя группы и функции
    String[] parts = split[0].split("\\.");
    if (parts.length != 2) {
      throw new JPNotFoundException("Function not found", "Функция не найдена");
    }
    Object[] values = null;
    if (split.length > 1) {
      values = new Object[split.length - 1];

      for (int j = 1; j < split.length; j++) {
        String p = split[j];
        values[j - 1] = data != null && data.containsKey(p) ? data.get(p) : p;
      }
    }
    return eval(parts[0], parts[1], values);
  }
}
