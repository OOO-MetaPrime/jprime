package mp.jprime.dataaccess.addinfos.services;

import mp.jprime.dataaccess.addinfos.AddInfo;
import mp.jprime.dataaccess.addinfos.JPObjectAddInfoParams;
import mp.jprime.dataaccess.addinfos.JPObjectAddInfoProvider;
import mp.jprime.dataaccess.addinfos.JPObjectAddInfoService;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.services.JPMetaStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Логика работы с дополнительными сведениями об объекте
 */
@Service
public final class JPObjectAddInfoCommonService implements JPObjectAddInfoService {
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

  private Collection<AddInfo> get(JPObjectAddInfoParams params) {
    return Stream.of(providers)
        .flatMap(Collection::stream)
        .map(x -> x.getAddInfo(params))
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }
}
