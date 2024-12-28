package mp.jprime.dataaccess.handlers;

import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.dataaccess.params.JPDelete;
import mp.jprime.dataaccess.params.JPUpdate;

/**
 * Базовая реализация хендлера
 */
public abstract class JPClassHandlerBase implements JPClassHandler {
  @Override
  public JPId find(JPCreate query) {
    return null;
  }

  @Override
  public void beforeCommitCreate(JPCreate query) {

  }

  @Override
  public void beforeCommitUpdate(JPUpdate query) {

  }

  @Override
  public void beforeCommitDelete(JPDelete query) {

  }

  @Override
  public void beforeCreate(JPCreate query) {

  }

  @Override
  public void beforeUpdate(JPUpdate query) {

  }

  @Override
  public void beforeDelete(JPDelete query) {

  }

  @Override
  public void afterCreate(Comparable newObjectId, JPCreate query) {

  }

  @Override
  public void afterUpdate(JPUpdate query) {

  }

  @Override
  public void afterDelete(JPDelete query) {

  }

  @Override
  public void afterCommitCreate(Comparable objectId, JPCreate query) {

  }

  @Override
  public void afterCommitUpdate(JPUpdate query) {

  }

  @Override
  public void afterCommitDelete(JPDelete query) {

  }
}
