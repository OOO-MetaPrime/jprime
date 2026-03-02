package mp.jprime.dataaccess.functions;

import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.dataaccess.functions.common.JPDateToWordsFunction;
import mp.jprime.dataaccess.functions.services.JPDataFunctionCommonService;
import mp.jprime.parsers.services.ParserCommonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    JPDataFunctionCommonService.class,
    JPDateToWordsFunction.class,
    ParserCommonService.class
})
public class JPDataFunctionServiceTest {
  @Autowired
  private JPDataFunctionService service;

  @Test
  void test() {
    JPMutableData data = JPMutableData.empty();
    data.put("data", LocalDate.of(2025, 1, 1));
    data.put("dataText", "${dateToWords.words_1$data}");

    service.eval(data);

    Assertions.assertEquals("первое января две тысячи двадцать пятого года", data.get("dataText"));
  }
}
