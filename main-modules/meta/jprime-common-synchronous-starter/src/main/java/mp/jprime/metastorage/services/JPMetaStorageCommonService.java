package mp.jprime.metastorage.services;

import mp.jprime.exceptions.JPClassMapNotFoundException;
import mp.jprime.exceptions.JPClassNotFoundException;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.metamaps.JPClassMap;
import mp.jprime.metamaps.services.JPMapsStorage;
import mp.jprime.repositories.JPStorage;
import mp.jprime.metastorage.JPMetaStorageService;
import mp.jprime.repositories.services.RepositoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервис работы с хранилищами
 */
@Service
public class JPMetaStorageCommonService implements JPMetaStorageService {
  /**
   * Описания хранилищ
   */
  private RepositoryStorage repoStorage;
  /**
   * Хранилище метаинформации
   */
  private JPMetaStorage metaStorage;
  /**
   * Описания привязки метаинформации к хранилищу
   */
  private JPMapsStorage mapsStorage;


  @Autowired
  private void setMetaStorage(JPMetaStorage metaStorage) {
    this.metaStorage = metaStorage;
  }

  @Autowired
  private void setMapsStorage(JPMapsStorage mapsStorage) {
    this.mapsStorage = mapsStorage;
  }

  @Autowired
  private void setRepoStorage(RepositoryStorage repoStorage) {
    this.repoStorage = repoStorage;
  }

  @Override
  public JPStorage getJpStorage(String classCode) {
    JPClass jpClass = metaStorage.getJPClassByCode(classCode);
    if (jpClass == null) {
      throw new JPClassNotFoundException(classCode);
    }
    // Получаем маппинг класса
    JPClassMap jpClassMap = mapsStorage.get(jpClass);
    if (jpClassMap == null) {
      throw new JPClassMapNotFoundException(classCode);
    }
    return repoStorage.getStorage(jpClassMap.getStorage());
  }
}
