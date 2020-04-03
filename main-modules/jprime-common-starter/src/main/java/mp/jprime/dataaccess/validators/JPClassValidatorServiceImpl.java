package mp.jprime.dataaccess.validators;

import mp.jprime.annotations.JPClassesLink;
import mp.jprime.common.JPClassesLinkFilter;
import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.dataaccess.params.JPDelete;
import mp.jprime.dataaccess.params.JPUpdate;
import mp.jprime.exceptions.JPRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Логика вызова валидаторов
 */
@Service
public final class JPClassValidatorServiceImpl implements JPClassValidatorService, JPClassesLinkFilter<JPObjectValidator> {
  private Map<String, Collection<JPObjectValidator>> jpClassValidators = new HashMap<>();

  /**
   * Считываем аннотации
   */
  @Autowired(required = false)
  private void setHanlders(Collection<JPObjectValidator> validators) {
    if (validators == null) {
      return;
    }
    Map<String, Collection<JPObjectValidator>> jpClassValidators = new HashMap<>();
    for (JPObjectValidator validator : validators) {
      try {
        JPClassesLink anno = validator.getClass().getAnnotation(JPClassesLink.class);
        if (anno == null) {
          continue;
        }
        for (String jpClassCode : anno.jpClasses()) {
          if (jpClassCode == null || jpClassCode.isEmpty()) {
            continue;
          }
          jpClassValidators.computeIfAbsent(jpClassCode, x -> new ArrayList<>()).add(validator);
        }
      } catch (Exception e) {
        throw JPRuntimeException.wrapException(e);
      }
    }

    for (String key : jpClassValidators.keySet()) {
      jpClassValidators.put(key, filter(jpClassValidators.get(key)));
    }

    this.jpClassValidators = jpClassValidators;
  }

  /**
   * Список валидаторов
   *
   * @param jpClassCode Кодовое имя класс
   * @return Список валидаторов
   */
  private Collection<JPObjectValidator> getValidators(String jpClassCode) {
    return jpClassValidators.get(jpClassCode);
  }

  /**
   * Перед созданием
   *
   * @param query JPCreate
   */
  @Override
  public void beforeCreate(JPCreate query) {
    // Запускаем кастомные валидаторы
    Collection<JPObjectValidator> validators = getValidators(query.getJpClass());
    if (validators != null) {
      validators.forEach(x -> x.beforeCreate(query));
    }
  }

  @Override
  public void beforeDelete(JPDelete query) {
    Collection<JPObjectValidator> validators = getValidators(query.getJpClass());
    if (validators != null) {
      validators.forEach(x -> x.beforeDelete(query));
    }
  }

  /**
   * Перед обновлением
   *
   * @param query JPUpdate
   */
  public void beforeUpdate(JPUpdate query) {
    // Запускаем кастомные валидаторы
    Collection<JPObjectValidator> validators = getValidators(query.getJpId().getJpClass());
    if (validators != null) {
      validators.forEach(x -> x.beforeUpdate(query));
    }
  }
}
