package mp.jprime.functions.base;

import mp.jprime.functions.JPFunction;
import mp.jprime.parsers.ParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;

/**
 * Сумма чисел
 * <p>
 * Параметры:
 * - произвольный набор чисел
 */
@Service
public class JPSumIntegerFunction implements JPFunction<Integer> {
  private ParserService parserService;

  @Autowired
  private void setParserService(ParserService parserService) {
    this.parserService = parserService;
  }

  @Override
  public Integer eval(Object... params) {
    if (params == null) {
      return null;
    }
    return Arrays.stream(params)
        .map(x -> parserService.parseTo(Integer.class, x))
        .filter(Objects::nonNull)
        .reduce(0, Integer::sum);
  }
}
