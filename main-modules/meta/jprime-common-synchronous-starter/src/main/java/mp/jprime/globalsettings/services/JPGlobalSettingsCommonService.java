package mp.jprime.globalsettings.services;

import mp.jprime.caches.services.JPBaseCache;
import mp.jprime.dataaccess.JPSyncObjectRepositoryService;
import mp.jprime.dataaccess.params.JPSelect;
import mp.jprime.globalsettings.JPGlobalProperty;
import mp.jprime.globalsettings.JPGlobalSettingsService;
import mp.jprime.globalsettings.events.JPGlobalSettingsChangeEvent;
import mp.jprime.globalsettings.events.JPGlobalSettingsLoadFinishEvent;
import mp.jprime.globalsettings.meta.JPGlobalSettingsCommonInnerMeta;
import mp.jprime.globalsettings.meta.JPGlobalSettingsInnerMeta;
import mp.jprime.globalsettings.storage.JPGlobalSettingsStorage;
import mp.jprime.parsers.ParserService;
import mp.jprime.repositories.RepositoryGlobalStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public final class JPGlobalSettingsCommonService extends JPBaseCache implements JPGlobalSettingsService {

  private final JPGlobalSettingsParserService gsService;
  private final JPSyncObjectRepositoryService repo;
  private final ParserService parserService;
  private final RepositoryGlobalStorage globalStorage;
  private final ApplicationEventPublisher eventPublisher;

  private volatile Map<String, JPGlobalSettingsParserService.JPGlobalSettings> cache = new ConcurrentHashMap<>();

  private JPGlobalSettingsCommonService(@Autowired JPGlobalSettingsParserService gsService,
                                        @Autowired JPSyncObjectRepositoryService repo,
                                        @Autowired ParserService parserService,
                                        @Autowired RepositoryGlobalStorage globalStorage,
                                        @Autowired ApplicationEventPublisher eventPublisher) {
    this.gsService = gsService;
    this.repo = repo;
    this.parserService = parserService;
    this.globalStorage = globalStorage;
    this.eventPublisher = eventPublisher;
  }

  @Override
  public String getCode() {
    return JPGlobalSettingsChangeEvent.Cache.CODE;
  }

  @Override
  public void load() {
    Map<String, JPGlobalSettingsParserService.JPGlobalSettings> result = new ConcurrentHashMap<>();

    boolean checkCache = false;
    if (globalStorage.getStorage(JPGlobalSettingsStorage.CODE) != null) {
      checkCache = true;

      repo.getList(
              JPSelect.from(JPGlobalSettingsInnerMeta.CLASS_CODE)
                  .attr(JPGlobalSettingsInnerMeta.Attr.CODE)
                  .attr(JPGlobalSettingsInnerMeta.Attr.PROPERTY)
                  .attr(JPGlobalSettingsInnerMeta.Attr.VALUE)
                  .build()
          ).stream()
          .map(gsService::toSettings)
          .forEach(
              x -> result.put(x.property().getCode(), x)
          );
    }
    if (globalStorage.getStorage(JPGlobalSettingsStorage.COMMON_CODE) != null) {
      checkCache = true;

      repo.getList(
              JPSelect.from(JPGlobalSettingsCommonInnerMeta.CLASS_CODE)
                  .attr(JPGlobalSettingsCommonInnerMeta.Attr.CODE)
                  .attr(JPGlobalSettingsCommonInnerMeta.Attr.PROPERTY)
                  .attr(JPGlobalSettingsCommonInnerMeta.Attr.VALUE)
                  .build()
          ).stream()
          .map(gsService::toSettings)
          .forEach(
              x -> result.put(x.property().getCode(), x)
          );
    }

    cache = result;

    if (checkCache) {
      eventPublisher.publishEvent(JPGlobalSettingsLoadFinishEvent.newEvent());
    }
  }

  @Override
  public JPGlobalProperty getProperty(String code) {
    JPGlobalSettingsParserService.JPGlobalSettings settings = cache.get(code);
    return settings != null ? settings.property() : null;
  }

  @Override
  public <T> T getValue(String code) {
    JPGlobalSettingsParserService.JPGlobalSettings settings = cache.get(code);
    return settings != null ? (T) parserService.parseTo(settings.property().getType().getJavaClass(), settings.value()) : null;
  }

  @Override
  public Collection<String> getSettingsCodeList() {
    return cache.keySet();
  }
}
