package mp.jprime.meta.services;

import mp.jprime.log.AppLogger;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPMetaDynamicLoader;
import mp.jprime.meta.annotations.services.JPMetaAnnoLoader;
import mp.jprime.meta.events.JPMetaLoadFinishEvent;
import mp.jprime.meta.log.Event;
import mp.jprime.meta.xmlloader.services.JPMetaXmlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Хранилище метаинформации
 */
@Service
@Lazy(value = false)
public final class JPMetaMemoryStorage implements JPMetaStorage {
  /**
   * Системный журнал
   */
  private AppLogger appLogger;

  private AtomicReference<Cache> cacheRef = new AtomicReference<Cache>() {
    {
      set(new Cache());
    }
  };

  /**
   * Публикация событий
   */
  private ApplicationEventPublisher eventPublisher;

  /**
   * Размещает метаописание в хранилище
   */
  private JPMetaMemoryStorage(@Autowired AppLogger appLogger,
                              @Autowired JPMetaAnnoLoader annoLoader,
                              @Autowired JPMetaXmlLoader xmlLoader) {
    this.appLogger = appLogger;

    Collection<Flux<Collection<JPClass>>> p = new ArrayList<>();
    p.add(annoLoader.load());
    p.add(xmlLoader.load());
    Flux.concat(p)
        .filter(Objects::nonNull)
        .subscribe(this::applyJPClasses);
  }

  @Autowired
  private void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  @Autowired(required = false)
  private void setDynamicLoader(Collection<JPMetaDynamicLoader> dynamicLoaders) {
    Flux
        .merge(
            dynamicLoaders.stream()
                .map(JPMetaDynamicLoader::load)
                .collect(Collectors.toList())
        )
        .filter(Objects::nonNull)
        .subscribe(this::applyDynamicJPClasses);
  }

  /**
   * Загружает список классов
   *
   * @param jpClasses Список классов
   */
  private void applyJPClasses(Collection<JPClass> jpClasses) {
    Cache cache = cacheRef.get();
    for (JPClass cls : jpClasses) {
      if (cache.codeJpClassMap.containsKey(cls.getCode())) {
        continue;
      }
      if (cls.getPrimaryKeyAttr() == null) {
        appLogger.error(Event.PRIMARY_KEY_NOT_FOUND, "JPClass \"" + cls.getCode() + "\" not loaded. Primary key is absent.");
        continue;
      }
      cache.classes.add(cls);
      cache.codeJpClassMap.put(cls.getCode(), cls);
      cache.pluralCodeJpClassMap.put(cls.getPluralCode(), cls);
    }
  }

  /**
   * Сохраняет указанный список классов
   *
   * @param list Список классов
   */
  private void applyDynamicJPClasses(Collection<JPClass> list) {
    if (list == null || list.isEmpty()) {
      return;
    }
    while (true) {
      Cache newCache = new Cache();

      Cache oldCache = cacheRef.get();
      // Добавляем постоянные настройки
      for (JPClass cls : oldCache.classes) {
        if (cls.isImmutable()) {
          newCache.classes.add(cls);
          newCache.codeJpClassMap.put(cls.getCode(), cls);
          newCache.pluralCodeJpClassMap.put(cls.getPluralCode(), cls);
        }
      }
      // Добавляем динамические настройки
      for (JPClass cls : list) {
        String code = cls.getCode();
        if (code == null || newCache.codeJpClassMap.containsKey(code)) {
          continue;
        }
        newCache.classes.add(cls);
        newCache.codeJpClassMap.put(code, cls);
        newCache.pluralCodeJpClassMap.put(cls.getPluralCode(), cls);
      }
      // Подменяем кеш после инициализации
      if (cacheRef.compareAndSet(oldCache, newCache)) {
        break;
      }
    }
    eventPublisher.publishEvent(JPMetaLoadFinishEvent.newEvent());
  }


  /**
   * Возвращает метаописания классов
   *
   * @return метаописания классов
   */
  @Override
  public Collection<JPClass> getJPClasses() {
    return cacheRef.get().umClasses;
  }

  /**
   * Возвращает метаописание класса по коду
   *
   * @param code Код класса
   * @return Метаописание класса
   */
  @Override
  public JPClass getJPClassByCode(String code) {
    return code == null ? null : cacheRef.get().codeJpClassMap.get(code);
  }

  /**
   * Возвращает метаописание класса по коду
   *
   * @param pluralCode Множественный код класса
   * @return Метаописание класса
   */
  @Override
  public JPClass getJPClassByPluralCode(String pluralCode) {
    return pluralCode == null ? null : cacheRef.get().pluralCodeJpClassMap.get(pluralCode);
  }

  private class Cache {
    private UUID uuid = UUID.randomUUID();
    /**
     * Список всей меты
     */
    private Collection<JPClass> classes = ConcurrentHashMap.newKeySet();
    private Collection<JPClass> umClasses = Collections.unmodifiableCollection(classes);
    /**
     * Код класса - класс
     */
    private Map<String, JPClass> codeJpClassMap = new ConcurrentHashMap<>();
    /**
     * Множественный код класса - класс
     */
    private Map<String, JPClass> pluralCodeJpClassMap = new ConcurrentHashMap<>();

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Cache cache = (Cache) o;
      return Objects.equals(uuid, cache.uuid);
    }

    @Override
    public int hashCode() {
      return Objects.hash(uuid);
    }
  }
}
