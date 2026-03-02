package mp.jprime.dataaccess.functions.beans;

import mp.jprime.dataaccess.functions.JPDataFunctionParams;

import java.util.Collections;
import java.util.List;

/**
 * Параметры функции
 */
public final class JPDataFunctionParamsBean implements JPDataFunctionParams {
  private final String sourceCode;
  private final int rowNum;
  private final String fieldCode;
  private final String template;
  private final List<Object> args;

  private JPDataFunctionParamsBean(String sourceCode, int rowNum, String fieldCode, String template, List<Object> args) {
    this.sourceCode = sourceCode;
    this.rowNum = rowNum;
    this.fieldCode = fieldCode;
    this.template = template;
    this.args = args != null ? Collections.unmodifiableList(args) : Collections.emptyList();
  }

  public static JPDataFunctionParams of(String sourceCode, int rowNum, String fieldCode, String template, List<Object> args) {
    return new JPDataFunctionParamsBean(sourceCode, rowNum, fieldCode, template, args);
  }

  @Override
  public String getSourceCode() {
    return sourceCode;
  }

  @Override
  public int getRowNum() {
    return rowNum;
  }

  @Override
  public String getFieldCode() {
    return fieldCode;
  }

  @Override
  public String getTemplate() {
    return template;
  }

  @Override
  public List<Object> getArgs() {
    return args;
  }
}
