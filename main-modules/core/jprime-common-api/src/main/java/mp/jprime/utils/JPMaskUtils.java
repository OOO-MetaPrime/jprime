package mp.jprime.utils;

import mp.jprime.exceptions.JPAppRuntimeException;
import mp.jprime.formats.JPStringFormat;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import static mp.jprime.formats.JPStringFormat.*;

/**
 * Применяет маску к значению
 * <p>
 * #	- любая цифра <br>
 * X	- любая цифра или буква латинского алфавита <br>
 * S	- любая буква латинского алфавита <br>
 * R	- любая буква русского алфавита <br>
 */
public final class JPMaskUtils {
  private static final Logger LOG = LoggerFactory.getLogger(JPMaskUtils.class);

  private static final Map<Character, String> TOKEN_REGEX = Map.of(
      '#', "\\d",
      'X', "[0-9A-Za-z]",
      'S', "[A-Za-z]",
      'R', "[А-Яа-яЁё]"
  );

  private static final Collection<Character> ALLOWED_DELIMITERS = Set.of(
      ' ', '*', '-', '!', '$', '%', '^', '&', '(', ')', '_', '+', '|', '~', '=', '`',
      '{', '}', '[', ']', ':', '"', ';', '\'', '<', '>', '?', ',', '.', '\\', '/'
  );

  private static final Map<JPStringFormat, String> MASKS = new EnumMap<>(JPStringFormat.class);

  static {
    MASKS.putIfAbsent(INN, "############");
    MASKS.putIfAbsent(INN_10, "############");
    MASKS.putIfAbsent(SNILS, "###-###-### ##");
    MASKS.putIfAbsent(OGRN, "#############");
  }

  /**
   * Применить маску к значению
   *
   * @param format Тип формата строки
   * @param value  Значение
   *
   * @return Значение с примененной маской
   */
  public static String apply(JPStringFormat format, String value) {
    if (format == null || StringUtils.isBlank(value)) {
      return value;
    }
    String mask = MASKS.get(format);
    if (mask == null) {
      returnThrow("null", value, String.format("Отсутствует маска для Типа формата строки %s.", format));
    }
    return apply(mask, value);
  }

  /**
   * Применить маску к значению
   *
   * @param mask  Маска
   * @param value Значение
   *
   * @return Значение с примененной маской
   */
  public static String apply(String mask, String value) {
    if (StringUtils.isAnyBlank(mask, value)) {
      return value;
    }

    StringBuilder result = new StringBuilder();
    int valueIndex = 0;

    for (int i = 0; i < mask.length(); i++) {
      char maskChar = mask.charAt(i);

      if (TOKEN_REGEX.containsKey(maskChar)) {
        if (valueIndex >= value.length()) {
          returnThrow(mask, value, "Кол-во токенов в маске превышает кол-во символов в значении.");
        }
        char valueChar = value.charAt(valueIndex);
        String regex = TOKEN_REGEX.get(maskChar);
        if (!String.valueOf(valueChar).matches(regex)) {
          returnThrow(mask, value, String.format("Символ '%s' не подходит под токен '%s'", valueChar, maskChar));
        }
        result.append(valueChar);
        valueIndex++;
      } else {
        // Вставляем разделитель
        if (ALLOWED_DELIMITERS.contains(maskChar)) {
          result.append(maskChar);
        } else {
          returnThrow(mask, value, String.format("В маске используется не разрешенный разделитель '%c'.", maskChar));
        }
      }
    }

    // Проверяем, что все символы из value использованы
    if (valueIndex != value.length()) {
      returnThrow(mask, value, "Кол-во символов в значении превышает кол-во токенов в маске.");
    }

    return result.toString();

  }

  private static void returnThrow(String mask, String value, String errMsg) {
    String fullErrMsg = getErr(mask, value, errMsg);
    LOG.error(fullErrMsg);
    throw new JPAppRuntimeException("jpmaskutils", fullErrMsg);
  }

  private static String getErr(String mask, String value, String errMsg) {
    return String.format("Не возможно применить маску %s к значению %s. %s", mask, value, errMsg);
  }
}
