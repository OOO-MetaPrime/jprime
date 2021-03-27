package mp.jprime.dataaccess.validators;

import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.dataaccess.params.JPDelete;
import mp.jprime.dataaccess.params.JPUpdate;
import reactor.core.publisher.Mono;

/**
 * Базовая реализация хендлера
 */
public abstract class JPReactiveClassValidatorBase implements JPReactiveClassValidator {
  /**
   * Перед созданием
   *
   * @param query JPCreate
   */
  @Override
  public Mono<Void> beforeCreate(JPCreate query) {
    return Mono.empty();
  }

  /**
   * Перед обновлением
   *
   * @param query JPUpdate
   */
  @Override
  public Mono<Void> beforeUpdate(JPUpdate query) {
    return Mono.empty();
  }

  /**
   * Перед удалением
   *
   * @param query JPDelete
   */
  @Override
  public Mono<Void> beforeDelete(JPDelete query) {
    return Mono.empty();
  }
}
