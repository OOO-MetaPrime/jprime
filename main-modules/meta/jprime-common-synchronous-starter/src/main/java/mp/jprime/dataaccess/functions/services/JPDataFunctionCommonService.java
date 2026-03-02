package mp.jprime.dataaccess.functions.services;

import mp.jprime.lang.JPMutableMap;
import mp.jprime.dataaccess.functions.JPDataFunction;
import mp.jprime.dataaccess.functions.JPDataFunctionParams;
import mp.jprime.dataaccess.functions.JPDataFunctionResult;
import mp.jprime.dataaccess.functions.JPDataFunctionService;
import mp.jprime.dataaccess.functions.beans.JPDataFunctionParamsBean;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.security.AuthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Работа с функциями формирования данных, доступными в системе
 */
@Service
public final class JPDataFunctionCommonService implements JPDataFunctionService {
  /**
   * Список всех функций в системе
   */
  private final Map<String, JPDataFunction> functions;

  @Autowired(required = false)
  private JPDataFunctionCommonService(Collection<JPDataFunction> functions) {
    this.functions = functions == null ? Collections.emptyMap() : Collections.unmodifiableMap(
        functions.stream().collect(Collectors.toMap(JPDataFunction::getCode, f -> f))
    );
  }

  @Override
  public void eval(Map<String, List<? extends JPMutableMap>> data, FormatConsumer formatConsumer, AuthInfo auth) {
    Map<String, Collection<JPDataFunctionParams>> functionArgs = getArgs(data);
    // Ключ - код функции, значение - результаты функции
    Map<JPDataFunctionParams, JPDataFunctionResult> functionResult = new HashMap<>();
    functionArgs.forEach((code, fArgs) -> {
      JPDataFunction func = functions.get(code);
      if (func == null) {
        return;
      }
      Map<JPDataFunctionParams, JPDataFunctionResult> results = func.eval(fArgs, auth);
      if (results != null) {
        functionResult.putAll(results);
      }
      fArgs.forEach(x -> functionResult.putIfAbsent(x, null));
    });
    replaceData(data, functionResult, formatConsumer);
  }

  /**
   * Выбирает поля-аргументы и их значения для текущей функции
   *
   * @param data данные
   * @return список объектов, содержащих аргументы и их значения, разделенных по источникам
   */
  private Map<String, Collection<JPDataFunctionParams>> getArgs(Map<String, List<? extends JPMutableMap>> data) {
    Map<String, Collection<JPDataFunctionParams>> result = new HashMap<>();

    data.forEach((blockCode, values) -> {
      if (values == null) {
        return;
      }
      for (int i = 0; i < values.size(); i++) {
        final int curNumber = i;
        JPMutableMap row = values.get(i);
        row.forEach((fieldCode, value) -> {
          checkAndExecute(value, (functionArgs, function) -> {
            String code = function.getCode();
            Collection<String> templates = function.getTemplates();

            String functionTemplate = functionArgs[0];
            List<String> functionParams = Arrays.stream(functionArgs)
                .filter(p -> !templates.contains(p))
                .toList();

            List<Object> args = new ArrayList<>();
            for (int j = 0; j < functionParams.size(); j++) {
              String argCode = function.getArgCode(j);
              if (argCode == null) {
                throw new JPRuntimeException("Неверные настройки: количество аргументов не соответствует функции '" + code + '\'');
              }
              String p = functionParams.get(j);
              // Если резалт-сет содержит поле, которое передано в вызове функции, то берется значение этого поля,
              // иначе - считается, что в функцию передается не ссылка на поле, а значение
              args.add(row.containsKey(p) ? row.get(p) : p);
            }

            result.computeIfAbsent(code, x -> new ArrayList<>())
                .add(JPDataFunctionParamsBean.of(blockCode, curNumber, fieldCode, functionTemplate, args));
          });
        });
      }
    });

    return result;
  }

  /**
   * Заменяет шаблоны на результаты работы функций
   *
   * @param data     данные
   * @param funcData результаты всех функций
   */
  private void replaceData(Map<String, List<? extends JPMutableMap>> data,
                           Map<JPDataFunctionParams, JPDataFunctionResult> funcData,
                           FormatConsumer formatConsumer) {
    if (funcData == null || funcData.isEmpty()) {
      return;
    }
    funcData.forEach((params, result) -> {
      List<? extends JPMutableMap> list = data.get(params.getSourceCode());
      if (list == null || list.isEmpty()) {
        return;
      }
      JPMutableMap row = params.getRowNum() < list.size() ? list.get(params.getRowNum()) : null;
      if (row == null || row.isEmpty()) {
        return;
      }
      row.put(params.getFieldCode(), result != null ? result.getResult() : null);

      if (formatConsumer != null && result != null && result.getFormat() != null) {
        formatConsumer.accept(params.getSourceCode(), params.getFieldCode(), result.getFormat());
      }
    });
  }

  /**
   * Проверяет, является ли значение вызовом текущей функции
   * и если да, то выполняет переданный консьюмер
   *
   * @param value    значение
   * @param consumer консьюмер
   */
  private void checkAndExecute(Object value, BiConsumer<String[], JPDataFunction> consumer) {
    if (value instanceof String template) {
      // Если значение поля выглядит как плейс-холдер
      if (isPlaceholder(template)) {
        // Режем содержимое фигурных скобок по '$'
        String[] split = template.substring(2, template.length() - 1).split("\\$");
        // Если получилось больше нуля подстрок
        if (split.length > 0) {
          // то достаем из первой подстроки имя функции и шаблона
          String[] invocation = split[0].split("\\.");
          if (invocation.length == 2 && functions.containsKey(invocation[0])) {
            JPDataFunction function = functions.get(invocation[0]);
            if (function.getTemplates().contains(invocation[1])) {
              // и подменяем первую подстроку в массиве с `<code>.<template>` на `<template>`
              split[0] = invocation[1];
              consumer.accept(split, function);
            }
          }
        }
      }
    }
  }

  private boolean isPlaceholder(String template) {
    return template.startsWith("${") && template.endsWith("}") && template.length() > 3;
  }
}
