package mp.jprime.parsers.base;

import mp.jprime.lang.JPIntegerArray;
import mp.jprime.parsers.ParserService;
import mp.jprime.parsers.ParserServiceAware;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Collection -> JPIntegerArray
 */
@Service
public class CollectionToJPIntegerArray implements TypeParser<Collection, JPIntegerArray>, ParserServiceAware {
  private ParserService service;

  @Override
  public void setParserService(ParserService service) {
    this.service = service;
  }

  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public JPIntegerArray parse(Collection value) {
    if (value == null) {
      return null;
    }
    return JPIntegerArray.of(
        ((Collection<Object>) value).stream()
            .map(x -> service.parseTo(Integer.class, x))
            .collect(Collectors.toList())
    );
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<Collection> getInputType() {
    return Collection.class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  public Class<JPIntegerArray> getOutputType() {
    return JPIntegerArray.class;
  }
}
