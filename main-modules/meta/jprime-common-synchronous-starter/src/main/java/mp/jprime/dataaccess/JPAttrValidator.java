package mp.jprime.dataaccess;

import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.dataaccess.params.*;
import mp.jprime.formats.JPStringFormat;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.parsers.ValueParser;
import mp.jprime.parsers.stringformat.JpStringFormatParser;
import mp.jprime.parsers.stringformat.JpStringFormatUtils;
import mp.jprime.parsers.stringformat.exceptions.JPStringFormatParseException;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.services.JPSecurityStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * Проверка доступа к атрибутам при операциях создания, изменения
 */
public interface JPAttrValidator {
  /**
   * Возвращает JPSecurityStorage
   *
   * @return JPSecurityStorage
   */
  JPSecurityStorage getSecurityStorage();

  /**
   * Проверка прав доступа к атрибутам при создании
   *
   * @param jpClass JPClass
   * @param query   Запрос
   */
  default void validateAttr(JPClass jpClass, JPCreate query) {
    validateAttr(
        jpClass, query,
        (jpPackage, roles) -> getSecurityStorage().checkCreate(jpPackage, roles)
    );
  }

  /**
   * Проверка прав доступа к атрибутам при обновлении
   *
   * @param jpClass JPClass
   * @param query   Запрос
   */
  default void validateAttr(JPClass jpClass, JPUpdate query) {
    validateAttrForUpdate(jpClass, query);
  }

  /**
   * Проверка прав доступа к атрибутам при обновлении
   *
   * @param jpClass JPClass
   * @param query   Запрос
   */
  default void validateAttr(JPClass jpClass, JPConditionalUpdate query) {
    validateAttrForUpdate(jpClass, query);
  }

  /**
   * Проверка прав доступа к атрибутам при обновлении
   *
   * @param jpClass JPClass
   * @param query   JPSave
   */
  default void validateAttrForUpdate(JPClass jpClass, JPSave query) {
    validateAttr(
        jpClass, query,
        (jpPackage, roles) -> getSecurityStorage().checkUpdate(jpPackage, roles)
    );
  }

  /**
   * Проверка прав доступа к атрибутам при создании
   *
   * @param jpClass JPClass
   * @param query   Запрос
   */
  default void validateAttr(JPClass jpClass, JPSave query,
                            BiFunction<String, Collection<String>, Boolean> func) {
    JPMutableData data = query.getData();
    AuthInfo auth = query.getAuth();
    Source source = query.getSource();

    Map<String, String> errorFields = null;
    Map<String, String> errorTitles = null;

    for (String attrCode : Set.copyOf(data.keySet())) {
      JPAttr jpAttr = jpClass.getAttr(attrCode);
      JPStringFormat stringFormat = jpAttr != null ? jpAttr.getStringFormat() : null;

      boolean isRemove;
      if (jpAttr == null) {
        isRemove = true;
      } else if (query.isSystemAttr(jpAttr.getCode())) {
        isRemove = false;
      } else {
        isRemove = source == Source.USER && auth != null &&
            !func.apply(jpAttr.getJpPackage(), auth.getRoles());
      }
      if (isRemove) {
        data.remove(attrCode);
      } else if (stringFormat != null) {
        String value = ValueParser.toString(data.get(attrCode));

        JpStringFormatParser.Result result = JpStringFormatUtils.parse(stringFormat, value);
        if (result.isCheck()) {
          data.put(attrCode, result.getParseValue());
        } else {
          errorFields = errorFields != null ? errorFields : new HashMap<>();
          errorTitles = errorTitles != null ? errorTitles : new HashMap<>();

          errorFields.put(attrCode, value);
          errorTitles.put(attrCode, jpAttr.getName());
        }
      }
    }

    if (errorFields != null && !errorFields.isEmpty()) {
      throw new JPStringFormatParseException(errorFields, errorTitles);
    }
  }

  /**
   * Проверка прав доступа к атрибутам при обновлении
   *
   * @param jpClass JPClass
   * @param query   JPBatchUpdate
   */
  default void validateAttr(JPClass jpClass, JPBatchUpdate query) {
    AuthInfo auth = query.getAuth();
    query.forEach((id, data) -> data.entrySet()
        .stream()
        .filter(entry -> {
          JPAttr jpAttr = jpClass.getAttr(entry.getKey());
          if (jpAttr == null) {
            return true;
          } else {
            return query.getSource() == Source.USER && auth != null &&
                !getSecurityStorage().checkUpdate(jpAttr.getJpPackage(), auth.getRoles());
          }
        })
        .map(Map.Entry::getKey)
        .toList()
        .forEach(data::remove));
  }
}
