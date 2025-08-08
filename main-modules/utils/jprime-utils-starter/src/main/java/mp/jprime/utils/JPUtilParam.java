package mp.jprime.utils;

import mp.jprime.common.JPEnum;
import mp.jprime.common.beans.JPParamBase;
import mp.jprime.files.FileType;
import mp.jprime.formats.JPStringFormat;
import mp.jprime.meta.JPMoney;
import mp.jprime.meta.beans.JPType;

import java.util.Collection;

/**
 * Бин параметра для утилиты
 */
public class JPUtilParam extends JPParamBase {
  /**
   * @param code         Название (Код) параметра
   * @param type         Тип параметра
   * @param stringFormat Тип строкового поля
   * @param stringMask   Маска строкового поля
   * @param fileTypes    Расширения файлов для выбора
   * @param length       Длина (для строковых полей)
   * @param description  Наименование
   * @param qName        QName
   * @param mandatory    Признак обязательности
   * @param multiple     Признак множественного выбора
   * @param refJpClass   Класс из меты
   * @param refJpAttr    Атрибут класса
   * @param refFilter    JSON для фильтрации объектов
   * @param money        Настройки денежного атрибута
   * @param external     Возможность внешнего переопределения параметра
   * @param value        Значение параметра
   * @param enums        Перечислимые значения
   * @param clientSearch Клиентский поиск
   * @param actionLog    Признак логирования значения
   * @param readOnly     Признак только для чтения
   */
  private JPUtilParam(String code, JPType type, JPStringFormat stringFormat, String stringMask,
                      Collection<FileType> fileTypes, Integer length, String description, String qName,
                      boolean mandatory, boolean multiple, String refJpClass, String refJpAttr, String refFilter,
                      JPMoney money,
                      boolean external, Object value, Collection<JPEnum> enums, boolean clientSearch, boolean actionLog,
                      boolean readOnly) {
    super(code, type, stringFormat, stringMask, fileTypes, length, description, qName, mandatory, multiple,
        refJpClass, refJpAttr, refFilter, money, external, value, enums, clientSearch, actionLog, readOnly);
  }


  public static Builder newBuilder() {
    return new Builder();
  }

  public final static class Builder extends JPParamBase.Builder<Builder> {
    private Builder() {
      super();
    }

    public JPUtilParam build() {
      return new JPUtilParam(code, type, stringFormat, stringMask, fileTypes, length, description, qName, mandatory, multiple, refJpClass,
          refJpAttr, refFilter, money, external, value, enums, clientSearch, actionLog, readOnly);
    }
  }
}
