package mp.jprime.attrparsers.base;

import mp.jprime.attrparsers.AttrTypeParser;
import mp.jprime.dataaccess.JPAttrData;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.lang.JPXmlString;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.beans.JPType;
import mp.jprime.parsers.exceptions.JPParseException;
import mp.jprime.xml.services.JPXmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Реализация парсера {@link JPType#XML}
 */
@Service
public class JPXmlParser implements AttrTypeParser<JPXmlString> {
  private static final JPType JP_TYPE = JPType.XML;

  private JPXmlMapper xmlMapper;

  @Autowired
  private void setXmlMapper(JPXmlMapper xmlMapper) {
    this.xmlMapper = xmlMapper;
  }

  @Override
  public JPXmlString parse(JPAttr jpAttr, JPAttrData data) {
    if (data == null || jpAttr == null || jpAttr.getValueType() != JP_TYPE) {
      return null;
    }
    Object attrValue = data.get(jpAttr);
    if (attrValue == null) {
      return null;
    }
    return parse(jpAttr, attrValue);
  }

  @Override
  public JPXmlString parse(JPAttr jpAttr, Object attrValue) {
    if (jpAttr == null || jpAttr.getValueType() != JP_TYPE) {
      return null;
    }
    String attrName = jpAttr.getName();
    try {
      if (attrValue instanceof String x) {
        check(x);
        return JPXmlString.from(x);
      } else if (attrValue instanceof JPXmlString x) {
        check(x.toString());
        return x;
      } else {
        throw new JPParseException("valueparseerror." + attrName, "Неверно указано значение поля " + attrName);
      }
    } catch (Exception e) {
      throw new JPParseException("valueparseerror." + attrName, "Неверно указано значение поля " + attrName);
    }
  }

  private void check(String value) {
    xmlMapper.toJsonNode(value);
  }

  @Override
  public void fill(JPAttr jpAttr, JPXmlString attrValue, JPMutableData data) {
    if (data == null || jpAttr == null || jpAttr.getValueType() != JP_TYPE) {
      return;
    }
    data.put(jpAttr.getCode(), attrValue);
  }

  @Override
  public JPType getJPType() {
    return JP_TYPE;
  }

  @Override
  public Class<JPXmlString> getOutputType() {
    return JPXmlString.class;
  }
}
