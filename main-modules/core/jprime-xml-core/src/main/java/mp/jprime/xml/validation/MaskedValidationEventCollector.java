package mp.jprime.xml.validation;


import jakarta.xml.bind.ValidationEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Если в логе значение с ошибкой длиной более 4 символов, маскируем значение символом * оставляя последние 4
 */
public final class MaskedValidationEventCollector extends ValidationEventCollector {

  private static final Pattern VALUE_PATTERN = Pattern.compile("([Vv]alue )'(.*?)'");

  @Override
  protected String getEventMessage(ValidationEvent event) {
    String msg = event.getMessage();
    if (StringUtils.isEmpty(msg)) {
      return msg;
    }
    StringBuilder result = new StringBuilder();
    Matcher matcher = VALUE_PATTERN.matcher(msg);
    while (matcher.find()) {
      String pfx = matcher.group(1);
      String value = matcher.group(2);
      if (value.length() > 4) {
        value = StringUtils.repeat('*', value.length() - 4) + value.substring(value.length() - 4);
      }
      matcher.appendReplacement(result, pfx + "'" + value + "'");
    }
    matcher.appendTail(result);
    return result.toString();
  }
}
