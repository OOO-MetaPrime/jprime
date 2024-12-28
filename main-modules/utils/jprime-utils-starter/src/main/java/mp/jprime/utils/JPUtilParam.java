package mp.jprime.utils;

import mp.jprime.common.JPEnum;
import mp.jprime.common.beans.JPParamBase;
import mp.jprime.meta.beans.JPStringFormat;
import mp.jprime.meta.beans.JPType;

import java.util.Collection;

/**
 * Бин параметра для утилиты
 */
public class JPUtilParam extends JPParamBase {
  /**
   * @param code         Название(Код) параметра
   * @param type         Тип параметра
   * @param stringFormat Тип строкового поля
   * @param stringMask   Маска строкового поля
   * @param length       Длина (для строковых полей)
   * @param mandatory    Признак обязательности
   * @param refJpClass   Класс из меты
   * @param refJpAttr    Атрибут класса
   * @param external     Возможность внешнего переопределения параметра
   * @param value        Значение параметра
   * @param enums        Перечислимые значения
   */
  private JPUtilParam(String code, JPType type, JPStringFormat stringFormat, String stringMask,
                      Integer length, String description, String qName,
                      boolean mandatory, boolean multiple, String refJpClass, String refJpAttr, String refFilter,
                      boolean external, Object value, Collection<JPEnum> enums, boolean clientSearch) {
    super(code, type, stringFormat, stringMask, length, description, qName, mandatory, multiple,
        refJpClass, refJpAttr, refFilter, external, value, enums, clientSearch);
  }


  public static Builder newBuilder() {
    return new Builder();
  }

  public final static class Builder extends JPParamBase.Builder<Builder> {
    private Builder() {
      super();
    }

    public JPUtilParam build() {
      return new JPUtilParam(code, type, stringFormat, stringMask, length, description, qName, mandatory, multiple, refJpClass,
          refJpAttr, refFilter, external, value, enums, clientSearch);
    }
  }
}
