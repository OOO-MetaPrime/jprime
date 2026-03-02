package mp.jprime.attrparsers.base;

import mp.jprime.meta.beans.JPType;
import org.springframework.stereotype.Service;

/**
 * реализация парсера {@link JPType#DATE_RANGE}
 */
@Service
public final class JPDateRangeParser extends JPDateRangeBaseParser {

  @Override
  public JPType getJPType() {
    return JPType.DATE_RANGE;
  }
}