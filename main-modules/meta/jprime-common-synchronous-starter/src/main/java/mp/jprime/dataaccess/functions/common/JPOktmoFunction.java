package mp.jprime.dataaccess.functions.common;

import mp.jprime.dataaccess.functions.JPDataBaseFunction;
import mp.jprime.dataaccess.functions.JPDataFunctionParams;
import mp.jprime.dataaccess.functions.JPDataFunctionResult;
import mp.jprime.dataaccess.functions.beans.JPDataFunctionResultBean;
import mp.jprime.formats.JPStringFormat;
import mp.jprime.parsers.ParserService;
import mp.jprime.parsers.stringformat.JpStringFormatParser;
import mp.jprime.security.AuthInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

/**
 * Функция работы с ОКТМО
 */
@Service
public final class JPOktmoFunction extends JPDataBaseFunction<String> {
  /**
   * Кодовое имя функции
   */
  private static final String CODE = "oktmo";

  /**
   * Шаблоны
   */
  private interface Template {
    // 8 значный ОТКМО с 2 ведущими цифрами, остальные нули
    String PREFIX2_8 = "prefix2_8";
    // 8 значный ОТКМО с 5 ведущими цифрами, остальные нули
    String PREFIX5_8 = "prefix5_8";
  }

  /**
   * Шаблоны для вызова функции
   */
  private static final Collection<String> TEMPLATES = List.of(
      Template.PREFIX2_8,
      Template.PREFIX5_8
  );

  /**
   * Аргументы
   */
  private interface Arg {
    // поле с ОКТМО
    String OKTMO = "oktmo";
  }

  /**
   * Кодовые имена аргументов функции
   */
  private static final List<String> ARG_CODES = Collections.singletonList(Arg.OKTMO);

  private final ParserService parserService;
  private final Map<String, Function<String, String>> templateFuncList;

  private JPOktmoFunction(@Autowired ParserService parserService,
                          @Autowired JpStringFormatParser stringFormatParser) {
    this.parserService = parserService;

    this.templateFuncList = Map.of(
        Template.PREFIX2_8, oktmo -> {
          JpStringFormatParser.Result result = stringFormatParser.parse(JPStringFormat.OKTMO_ANY, oktmo);
          if (result.isCheck()) {
            String value = result.getParseValue();
            return StringUtils.rightPad(value.substring(0, 2), 8, "0");
          } else {
            return null;
          }
        },
        Template.PREFIX5_8, oktmo -> {
          JpStringFormatParser.Result result = stringFormatParser.parse(JPStringFormat.OKTMO_ANY, oktmo);
          if (result.isCheck()) {
            String value = result.getParseValue();
            return StringUtils.rightPad(value.substring(0, 5), 8, "0");
          } else {
            return null;
          }
        }
    );
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
  protected Map<JPDataFunctionParams, JPDataFunctionResult<String>> compute(Collection<JPDataFunctionParams> args, AuthInfo auth) {
    Map<JPDataFunctionParams, JPDataFunctionResult<String>> result = new HashMap<>(args.size());
    args.forEach(x -> {
      String oktmo = parserService.parseTo(String.class, x.getArgs().get(0));
      Function<String, String> func = oktmo != null ? templateFuncList.get(x.getTemplate()) : null;

      String value = null;
      if (func != null) {
        value = func.apply(oktmo);
      }
      result.put(x, JPDataFunctionResultBean.of(value));
    });
    return result;
  }
}
