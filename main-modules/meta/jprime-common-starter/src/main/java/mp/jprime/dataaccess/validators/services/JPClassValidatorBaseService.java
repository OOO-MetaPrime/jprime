package mp.jprime.dataaccess.validators.services;

import mp.jprime.annotations.JPClassesLink;
import mp.jprime.common.JPClassesLinkFilter;
import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.dataaccess.params.JPDelete;
import mp.jprime.dataaccess.params.JPUpdate;
import mp.jprime.dataaccess.validators.JPClassValidator;
import mp.jprime.dataaccess.validators.JPClassValidatorService;
import mp.jprime.dataaccess.validators.JPClassValidatorServiceAware;
import mp.jprime.exceptions.JPRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Логика вызова валидаторов
 */
@Service
public final class JPClassValidatorBaseService implements JPClassValidatorService, JPClassesLinkFilter<JPClassValidator> {

  /**
   * Указание ссылок
   */
  @Autowired(required = false)
  private void setAwares(Collection<JPClassValidatorServiceAware> awares) {
    for (JPClassValidatorServiceAware aware : awares) {
      aware.setJpClassValidatorService(this);
    }
  }

  private Map<String, Collection<JPClassValidator>> jpClassValidators = new HashMap<>();

  /**
   * Считываем аннотации
   */
  @Autowired(required = false)
  private void setHanlders(Collection<JPClassValidator> validators) {
    if (validators == null) {
      return;
    }
    Map<String, Collection<JPClassValidator>> jpClassValidators = new HashMap<>();
    for (JPClassValidator validator : validators) {
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
  private Collection<JPClassValidator> getValidators(String jpClassCode) {
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
    Collection<JPClassValidator> validators = getValidators(query.getJpClass());
    if (validators != null) {
      validators.forEach(x -> x.beforeCreate(query));
    }
  }

  @Override
  public void beforeDelete(JPDelete query) {
    Collection<JPClassValidator> validators = getValidators(query.getJpClass());
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
    Collection<JPClassValidator> validators = getValidators(query.getJpId().getJpClass());
    if (validators != null) {
      validators.forEach(x -> x.beforeUpdate(query));
    }
  }
}
