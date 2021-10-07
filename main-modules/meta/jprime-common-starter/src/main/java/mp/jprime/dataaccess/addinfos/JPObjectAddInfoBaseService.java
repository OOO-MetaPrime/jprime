package mp.jprime.dataaccess.addinfos;

import mp.jprime.meta.JPClass;
import mp.jprime.meta.services.JPMetaStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Логика работы с дополнительными сведениями об объекте
 */
@Service
public final class JPObjectAddInfoBaseService implements JPObjectAddInfoService {
  private Collection<JPObjectAddInfoProvider> providers;
  private JPMetaStorage metaStorage;

  /**
   * Считываем реализации
   */
  @Autowired(required = false)
  private void setDefValues(Collection<JPObjectAddInfoProvider> providers) {
    this.providers = providers != null ? providers : Collections.emptyList();
  }

  @Autowired
  private void setMetaStorage(JPMetaStorage metaStorage) {
    this.metaStorage = metaStorage;
  }

  /**
   * Возвращает сведения об объекте
   *
   * @param params Параметры для получения сведений
   */
  @Override
  public Collection<AddInfo> getAddInfo(JPObjectAddInfoParams params) {
    String jpClassCode = params.getJpClassCode();
    JPClass jpClass = jpClassCode != null ? metaStorage.getJPClassByCode(jpClassCode) : null;
    Object id = params.getId();
    if (jpClass == null || id == null) {
      return Collections.emptyList();
    }
    return get(params);
  }

  /**
   * Возвращает сведения об объекте
   *
   * @param params Параметры для получения сведений
   */
  @Override
  public Flux<AddInfo> getAsyncAddInfo(JPObjectAddInfoParams params) {
    String jpClassCode = params.getJpClassCode();
    JPClass jpClass = jpClassCode != null ? metaStorage.getJPClassByCode(jpClassCode) : null;
    Object id = params.getId();
    if (jpClass == null || id == null) {
      return Flux.empty();
    }
    return Flux.fromIterable(get(params));
  }

  private Collection<AddInfo> get(JPObjectAddInfoParams params) {
    return Stream.of(providers)
        .flatMap(Collection::stream)
        .map(x -> x.getAddInfo(params))
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }
}
