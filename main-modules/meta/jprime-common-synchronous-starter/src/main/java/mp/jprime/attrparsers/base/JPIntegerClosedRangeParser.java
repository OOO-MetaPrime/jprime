package mp.jprime.attrparsers.base;

import mp.jprime.meta.beans.JPType;
import org.springframework.stereotype.Service;

/**
 * реализация парсера {@link JPType#INT_CLOSED_RANGE}
 */
@Service
public final class JPIntegerClosedRangeParser extends JPIntegerRangeBaseParser {

  @Override
  public JPType getJPType() {
    return JPType.INT_CLOSED_RANGE;
  }
}