package mp.jprime.utils;

import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.formats.JPStringFormat;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Проверка допустимых масок для строковых значений
 */
public class JPMaskUtilsTest {

  @Test
  public void apply_Mask_ReturnValueWithMask() {
    assertEquals("12-Ыы-Ll-0R", JPMaskUtils.apply("##-RR-SS-XX", "12ЫыLl0R"));
  }

  @Test
  public void apply_SnilsMask_ReturnValueWithMask() {
    assertEquals("123-456-789 12", JPMaskUtils.apply("###-###-### ##", "12345678912"));
  }

  @Test
  void apply_NullMask_ReturnsOriginalValue() {
    String mask = null;
    assertEquals("test", JPMaskUtils.apply(mask, "test"));
  }

  @Test
  void apply_EmptyValue_ReturnsOriginalValue() {
    assertEquals("", JPMaskUtils.apply("##", ""));
  }

  @Test
  void apply_BlankMask_ReturnsOriginalValue() {
    assertEquals("123", JPMaskUtils.apply("   ", "123"));
  }

  // Специальные разделители
  @Test
  void apply_SpecialDelimiters_HandlesCorrectly() {
    String result = JPMaskUtils.apply("#!X/$R", "1FБ");
    assertEquals("1!F/$Б", result);
  }

  // Проверка сообщений об ошибках
  @Test
  void apply_InvalidValue1_ReturnError() {
    JPRuntimeException jpThrow = assertThrows(JPRuntimeException.class,
        () -> JPMaskUtils.apply("###", "12$"));
    assertEquals(jpThrow.getMessage(), "Не возможно применить маску ### к значению 12$. Символ '$' не подходит под токен '#'");
  }

  @Test
  void apply_InvalidValue2_ReturnError() {
    JPRuntimeException jpThrow = assertThrows(JPRuntimeException.class,
        () -> JPMaskUtils.apply("XXX", "55"));
    assertEquals(jpThrow.getMessage(), "Не возможно применить маску XXX к значению 55. Кол-во токенов в маске превышает кол-во символов в значении.");
  }

  @Test
  void apply_InvalidValue3_ReturnError() {
    JPRuntimeException jpThrow = assertThrows(JPRuntimeException.class,
        () -> JPMaskUtils.apply("XX", "123"));
    assertEquals(jpThrow.getMessage(), "Не возможно применить маску XX к значению 123. Кол-во символов в значении превышает кол-во токенов в маске.");
  }

  @Test
  void apply_InvalidDelimiter_ReturnError() {
    JPRuntimeException jpThrow = assertThrows(JPRuntimeException.class,
        () -> JPMaskUtils.apply("X@X", "12"));
    assertEquals(jpThrow.getMessage(), "Не возможно применить маску X@X к значению 12. В маске используется не разрешенный разделитель '@'.");
  }

  // Проверка JPStringFormat
  @Test
  void apply_JPStringFormatSNILS_ReturnsValueWithMask() {
    assertEquals("123-123-123 12", JPMaskUtils.apply(JPStringFormat.SNILS, "12312312312"));
  }

  @Test
  void apply_UnsupportedJPStringFormat_ReturnError() {
    JPRuntimeException jpThrow = assertThrows(JPRuntimeException.class,
        () -> JPMaskUtils.apply(JPStringFormat.BIK, "123456789"));
    assertEquals(jpThrow.getMessage(), "Не возможно применить маску null к значению 123456789. Отсутствует маска для Типа формата строки BIK.");
  }
}
