package mp.jprime.globalsettings.services;

import mp.jprime.beans.JPPropertyType;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.exceptions.JPAppRuntimeException;
import mp.jprime.globalsettings.JPGlobalProperty;
import mp.jprime.globalsettings.json.db.beans.JsonDbGlobalValue;
import mp.jprime.globalsettings.json.db.beans.JsonDbGlobalProperty;
import mp.jprime.globalsettings.meta.JPGlobalSettingsBaseInnerMeta;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.lang.JPIntegerArray;
import mp.jprime.lang.JPJsonNode;
import mp.jprime.lang.JPStringArray;
import mp.jprime.parsers.ParserService;
import mp.jprime.parsers.ParserServiceAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Утилитарный класс содержащий общую логику для работы сервисов
 */
@Service
public class JPGlobalSettingsParserService implements ParserServiceAware {

  private static final String BAD_REQUEST_CODE = "mp.jprime.globalsettings.badRequest";
  private static final String INCORRECT_SETTING_VALUE = "Некорректное значение настройки";

  private ParserService parserService;
  private JPJsonMapper jsonMapper;

  @Override
  public void setParserService(ParserService parserService) {
    this.parserService = parserService;
  }

  @Autowired
  private void setJsonMapper(JPJsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  public JPGlobalSettings toSettings(JPObject jpObject) {
    if (jpObject == null) {
      return null;
    }

    JsonDbGlobalProperty jsonProperty = jsonMapper.toObject(
        JsonDbGlobalProperty.class, parserService.parseTo(JPJsonNode.class, jpObject.getAttrValue(JPGlobalSettingsBaseInnerMeta.Attr.PROPERTY))
    );

    JPGlobalProperty property = JsonDbGlobalProperty.toJPGlobalProperty(jsonProperty);
    if (property == null) {
      return null;
    }

    JsonDbGlobalValue jsonValue = jsonMapper.toObject(
        JsonDbGlobalValue.class, parserService.parseTo(JPJsonNode.class, jpObject.getAttrValue(JPGlobalSettingsBaseInnerMeta.Attr.VALUE)));
    if (jsonValue == null) {
      return new JPGlobalSettings(property, null);
    } else {
      Object value = getValue(property.getType(), jsonValue);
      return new JPGlobalSettings(property, value);
    }
  }

  private Object getValue(JPPropertyType type, JsonDbGlobalValue jsonValue) {
    if (type == null) {
      return null;
    }
    if (type.equals(JPPropertyType.BOOLEAN)) {
      return parserService.parseTo(Boolean.class, jsonValue.getBooleanValue());
    } else if (type.equals(JPPropertyType.INT)) {
      return parserService.parseTo(Integer.class, jsonValue.getIntValue());
    } else if (type.equals(JPPropertyType.STRING)) {
      return parserService.parseTo(String.class, jsonValue.getStringValue());
    } else if (type.equals(JPPropertyType.INT_ARRAY)) {
      return parserService.parseTo(JPIntegerArray.class, jsonValue.getIntArrayValue());
    } else if (type.equals(JPPropertyType.STRING_ARRAY)) {
      return parserService.parseTo(JPStringArray.class, jsonValue.getStringArrayValue());
    }
    return null;
  }

  public Object getAndCheckValue(JPGlobalProperty property, Object value) {
    JPPropertyType type = property.getType();
    boolean isMandatory = property.isMandatory();
    boolean isReadonly = property.isReadonly();
    if (isReadonly) {
      throw new JPAppRuntimeException(BAD_REQUEST_CODE, "Настройка не может быть изменена");
    }
    if (value == null && isMandatory) {
      throw new JPAppRuntimeException(BAD_REQUEST_CODE, "Не указано обязательное значение настройки");
    }

    switch (type) {
      case BOOLEAN -> {
        return getBoolean(value);
      }
      case INT -> {
        Integer min = property.getMin();
        Integer max = property.getMax();

        Integer integerValue = getInteger(value);
        if (integerValue != null) {
          if ((min != null && integerValue < min) || (max != null && integerValue > max)) {
            throw new JPAppRuntimeException(BAD_REQUEST_CODE, "Значение не соответствует ограничениям");
          }
        } else if (min != null) {
          throw new JPAppRuntimeException(BAD_REQUEST_CODE, "Значение не соответствует ограничениям");
        }
        return integerValue;
      }
      case STRING -> {
        return getString(value);
      }
      case INT_ARRAY -> {
        Integer min = property.getMin();
        Integer max = property.getMax();

        Collection<Integer> values = getIntegerList(value);
        if (values != null) {
          if ((min != null && values.size() < min) || (max != null && values.size() > max)) {
            throw new JPAppRuntimeException(BAD_REQUEST_CODE, "Значение не соответствует ограничениям");
          }
        } else if (min != null) {
          throw new JPAppRuntimeException(BAD_REQUEST_CODE, "Значение не соответствует ограничениям");
        }
        return values;
      }
      case STRING_ARRAY -> {
        Integer min = property.getMin();
        Integer max = property.getMax();

        Collection<String> values = getStringList(value);
        if (values != null) {
          if ((min != null && values.size() < min) || (max != null && values.size() > max)) {
            throw new JPAppRuntimeException(BAD_REQUEST_CODE, "Значение не соответствует ограничениям");
          }
        } else if (min != null) {
          throw new JPAppRuntimeException(BAD_REQUEST_CODE, "Значение не соответствует ограничениям");
        }
        return values;
      }
      default ->
          throw new JPAppRuntimeException(BAD_REQUEST_CODE, String.format("Неподдерживаемый тип настройки: %s", type));
    }
  }

  public JsonDbGlobalValue toDbGlobalValue(JPGlobalProperty property, Object value) {
    JPPropertyType type = property.getType();

    JsonDbGlobalValue jsonValue = new JsonDbGlobalValue();

    switch (type) {
      case BOOLEAN -> jsonValue.setBooleanValue(parserService.parseTo(Boolean.class, value));
      case INT -> jsonValue.setIntValue(parserService.parseTo(Integer.class, value));
      case STRING -> jsonValue.setStringValue(getString(value));
      case INT_ARRAY -> jsonValue.setIntArrayValue((Collection<Integer>) value);
      case STRING_ARRAY -> jsonValue.setStringArrayValue((Collection<String>) value);
      default ->
          throw new JPAppRuntimeException(BAD_REQUEST_CODE, String.format("Неподдерживаемый тип настройки: %s", type));
    }
    return jsonValue;
  }

  private Boolean getBoolean(Object value) {
    if (value == null) {
      return Boolean.FALSE;
    }
    if (parserService.isParsable(Boolean.class, value)) {
      return parserService.parseTo(Boolean.class, value);
    }
    throw new JPAppRuntimeException(BAD_REQUEST_CODE, INCORRECT_SETTING_VALUE);
  }

  private Integer getInteger(Object value) {
    if (value == null) {
      return null;
    }
    if (value instanceof Number && parserService.isParsable(Integer.class, value)) {
      return parserService.parseTo(Integer.class, value);
    }
    throw new JPAppRuntimeException(BAD_REQUEST_CODE, INCORRECT_SETTING_VALUE);
  }

  private String getString(Object value) {
    if (value == null) {
      return null;
    }
    if (value instanceof String strValue && !strValue.isEmpty()) {
      return parserService.parseTo(String.class, value);
    }
    throw new JPAppRuntimeException(BAD_REQUEST_CODE, INCORRECT_SETTING_VALUE);
  }

  private Collection<Integer> getIntegerList(Object value) {
    if (value == null) {
      return null;
    }
    if (parserService.isParsable(JPIntegerArray.class, value) && parserService.isParsable(Collection.class, value)) {
      Collection<?> obj = parserService.parseTo(Collection.class, value);
      if (!obj.isEmpty() && obj.stream().allMatch(Number.class::isInstance)) {
        return parserService.parseTo(JPIntegerArray.class, value).toList();
      }
    }
    throw new JPAppRuntimeException(BAD_REQUEST_CODE, INCORRECT_SETTING_VALUE);
  }

  private Collection<String> getStringList(Object value) {
    if (value == null) {
      return null;
    }
    if (parserService.isParsable(JPStringArray.class, value) && parserService.isParsable(Collection.class, value)) {
      Collection<?> obj = parserService.parseTo(Collection.class, value);
      if (!obj.isEmpty() && obj.stream().allMatch(String.class::isInstance)) {
        return parserService.parseTo(JPStringArray.class, value).toList();
      }
    }
    throw new JPAppRuntimeException(BAD_REQUEST_CODE, INCORRECT_SETTING_VALUE);
  }

  public record JPGlobalSettings(JPGlobalProperty property, Object value) {
  }
}