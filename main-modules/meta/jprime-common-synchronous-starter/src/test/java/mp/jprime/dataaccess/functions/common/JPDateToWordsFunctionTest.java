package mp.jprime.dataaccess.functions.common;

import mp.jprime.dataaccess.functions.JPDataFunctionParams;
import mp.jprime.dataaccess.functions.JPDataFunctionResult;
import mp.jprime.dataaccess.functions.beans.JPDataFunctionParamsBean;
import mp.jprime.parsers.services.ParserCommonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    JPDateToWordsFunction.class, ParserCommonService.class
})
public class JPDateToWordsFunctionTest {

  @Autowired
  private JPDateToWordsFunction func;

  @Test
  void test() {
    Map<JPDataFunctionParams, JPDataFunctionResult<String>> result = func.eval(List.of(
        JPDataFunctionParamsBean.of("test", 1, "test", JPDateToWordsFunction.Template.FORMAT_1,
            List.of(LocalDate.of(2000, 1, 1)))), null);
    assertEquals(1, result.size());
    Assertions.assertEquals("01 января 2000 г.", result.values().iterator().next().getResult());

    result = func.eval(List.of(
        JPDataFunctionParamsBean.of("test", 1, "test", JPDateToWordsFunction.Template.FORMAT_2,
            List.of(LocalDate.of(2000, 1, 1)))), null);
    assertEquals(1, result.size());
    Assertions.assertEquals("01 января 2000 года", result.values().iterator().next().getResult());

    result = func.eval(List.of(
        JPDataFunctionParamsBean.of("test", 1, "test", JPDateToWordsFunction.Template.FORMAT_3,
            List.of(LocalDate.of(2000, 1, 1)))), null);
    assertEquals(1, result.size());
    Assertions.assertEquals("1 января 2000 г.", result.values().iterator().next().getResult());

    result = func.eval(List.of(
        JPDataFunctionParamsBean.of("test", 1, "test", JPDateToWordsFunction.Template.FORMAT_4,
            List.of(LocalDate.of(2000, 1, 1)))), null);
    assertEquals(1, result.size());
    Assertions.assertEquals("1 января 2000 года", result.values().iterator().next().getResult());

    result = func.eval(List.of(
        JPDataFunctionParamsBean.of("test", 1, "test", JPDateToWordsFunction.Template.WORDS_1,
            List.of(LocalDate.of(2000, 1, 1)))), null);
    assertEquals(1, result.size());
    Assertions.assertEquals("первое января двухтысячного года", result.values().iterator().next().getResult());

    result = func.eval(List.of(
        JPDataFunctionParamsBean.of("test", 1, "test", JPDateToWordsFunction.Template.WORDS_1,
            List.of(LocalDate.of(2001, 1, 1)))), null);
    assertEquals(1, result.size());
    Assertions.assertEquals("первое января две тысячи первого года", result.values().iterator().next().getResult());
  }
}
