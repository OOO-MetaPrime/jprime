package mp.jprime.dataaccess.validators.services;

import mp.jprime.annotations.JPClassesLink;
import mp.jprime.common.JPClassesLinkFilter;
import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.dataaccess.params.JPDelete;
import mp.jprime.dataaccess.params.JPUpdate;
import mp.jprime.dataaccess.validators.JPReactiveClassValidator;
import mp.jprime.dataaccess.validators.JPReactiveClassValidatorService;
import mp.jprime.exceptions.JPRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Логика вызова валидаторов
 */
@Service
public final class JPReactiveClassValidatorBaseService implements JPReactiveClassValidatorService, JPClassesLinkFilter<JPReactiveClassValidator> {
  private Map<String, Collection<JPReactiveClassValidator>> jpClassValidators = new HashMap<>();

  /**
   * Считываем аннотации
   */
  @Autowired(required = false)
  private void setHanlders(Collection<JPReactiveClassValidator> validators) {
    if (validators == null) {
      return;
    }
    Map<String, Collection<JPReactiveClassValidator>> jpClassValidators = new HashMap<>();
    for (JPReactiveClassValidator validator : validators) {
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
  private Collection<JPReactiveClassValidator> getValidators(String jpClassCode) {
    return jpClassValidators.get(jpClassCode);
  }

  /**
   * Перед созданием
   *
   * @param query JPCreate
   */
  @Override
  public Mono<Void> beforeCreate(JPCreate query) {
    // Запускаем кастомные валидаторы
    Collection<JPReactiveClassValidator> validators = getValidators(query.getJpClass());
    return validators == null || validators.isEmpty() ? Mono.empty() : Mono.when(
        validators
            .stream()
            .map(x -> x.beforeCreate(query))
            .collect(Collectors.toList())
    );
  }

  @Override
  public Mono<Void> beforeDelete(JPDelete query) {
    Collection<JPReactiveClassValidator> validators = getValidators(query.getJpClass());
    return validators == null || validators.isEmpty() ? Mono.empty() : Mono.when(
        validators
            .stream()
            .map(x -> x.beforeDelete(query))
            .collect(Collectors.toList())
    );
  }

  /**
   * Перед обновлением
   *
   * @param query JPUpdate
   */
  public Mono<Void> beforeUpdate(JPUpdate query) {
    Collection<JPReactiveClassValidator> validators = getValidators(query.getJpClass());
    return validators == null || validators.isEmpty() ? Mono.empty() : Mono.when(
        validators
            .stream()
            .map(x -> x.beforeUpdate(query))
            .collect(Collectors.toList())
    );
  }
}
