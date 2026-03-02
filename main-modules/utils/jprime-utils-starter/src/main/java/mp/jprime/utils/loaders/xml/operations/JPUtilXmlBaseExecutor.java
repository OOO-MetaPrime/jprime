package mp.jprime.utils.loaders.xml.operations;

import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.utils.JPStringUtils;
import mp.jprime.utils.loaders.xml.JPUtilXmlOperation;

import java.util.Map;

/**
 * Базовая логика для реализации JPUtilXmlOperation.Executor
 */
public abstract class JPUtilXmlBaseExecutor implements JPUtilXmlOperation.Executor {
  protected String replaceParamValues(String s, Map<String, Object> paramValues) {
    return JPStringUtils.replaceParamValues(s, paramValues.keySet(), paramValues::get);
  }

  protected String replaceAttrValues(String s, JPObject object) {
    return JPStringUtils.replaceAttrValues(s, object.getData().keySet(), object::getAttrValue);
  }
}
