package mp.jprime.attrparsers.base;

import mp.jprime.meta.beans.JPType;
import org.springframework.stereotype.Service;

/**
 * реализация парсера {@link JPType#DATETIME_CLOSED_RANGE}
 */
@Service
public final class JPDateTimeClosedRangeParser extends JPDateTimeRangeBaseParser {

  @Override
  public JPType getJPType() {
    return JPType.DATETIME_CLOSED_RANGE;
  }
}