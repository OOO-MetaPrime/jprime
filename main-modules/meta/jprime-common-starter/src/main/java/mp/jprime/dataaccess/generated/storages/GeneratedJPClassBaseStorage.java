package mp.jprime.dataaccess.generated.storages;

import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.generated.GeneratedJPClassStorage;
import mp.jprime.dataaccess.params.*;
import mp.jprime.exceptions.JPUnsupportedOperationException;

import java.util.Collection;

/**
 * Базовая реализация GeneratedJPClassStorage
 */
public abstract class GeneratedJPClassBaseStorage implements GeneratedJPClassStorage {
  @Override
  public Long getTotalCount(JPSelect select) {
    throw new JPUnsupportedOperationException();
  }

  @Override
  public Collection<JPObject> getList(JPSelect select) {
    throw new JPUnsupportedOperationException();
  }

  @Override
  public JPData getAggregate(JPAggregate aggr) {
    throw new JPUnsupportedOperationException();
  }

  @Override
  public JPId create(JPCreate query) {
    throw new JPUnsupportedOperationException();
  }

  @Override
  public JPObject createAndGet(JPCreate query) {
    throw new JPUnsupportedOperationException();
  }

  @Override
  public JPId update(JPUpdate query) {
    throw new JPUnsupportedOperationException();
  }

  @Override
  public Long update(JPConditionalUpdate query) {
    throw new JPUnsupportedOperationException();
  }

  @Override
  public JPObject updateAndGet(JPUpdate query) {
    throw new JPUnsupportedOperationException();
  }

  @Override
  public JPId patch(JPCreate query) {
    throw new JPUnsupportedOperationException();
  }

  @Override
  public JPObject patchAndGet(JPCreate query) {
    throw new JPUnsupportedOperationException();
  }

  @Override
  public Long delete(JPDelete query) {
    throw new JPUnsupportedOperationException();
  }

  @Override
  public Long delete(JPConditionalDelete query) {
    throw new JPUnsupportedOperationException();
  }
}
