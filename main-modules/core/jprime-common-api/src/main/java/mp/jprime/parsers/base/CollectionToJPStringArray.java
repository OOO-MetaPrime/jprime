package mp.jprime.parsers.base;

import mp.jprime.lang.JPStringArray;
import mp.jprime.parsers.ParserService;
import mp.jprime.parsers.ParserServiceAware;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Collection -> JPStringArray
 */
@Service
public final class CollectionToJPStringArray implements TypeParser<Collection, JPStringArray>, ParserServiceAware {
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
  public JPStringArray parse(Collection value) {
    if (value == null) {
      return null;
    }
    return JPStringArray.of(
        ((Collection<Object>) value).stream()
            .map(x -> service.parseTo(String.class, x))
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
  public Class<JPStringArray> getOutputType() {
    return JPStringArray.class;
  }
}
