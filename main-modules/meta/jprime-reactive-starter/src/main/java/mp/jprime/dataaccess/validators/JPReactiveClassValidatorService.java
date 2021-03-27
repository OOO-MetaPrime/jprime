package mp.jprime.dataaccess.validators;

import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.dataaccess.params.JPDelete;
import mp.jprime.dataaccess.params.JPUpdate;
import reactor.core.publisher.Mono;

/**
 * Логика вызова валидаторов
 */
public interface JPReactiveClassValidatorService {
  /**
   * Перед созданием
   *
   * @param query JPCreate
   */
  Mono<Void> beforeCreate(JPCreate query);

  /**
   * Перед обновлением
   *
   * @param query JPUpdate
   */
  Mono<Void> beforeUpdate(JPUpdate query);

  /**
   * Перед удалением
   *
   * @param query JPDelete
   */
  Mono<Void> beforeDelete(JPDelete query);
}
