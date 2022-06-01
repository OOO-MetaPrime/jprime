package mp.jprime.meta.services;

import mp.jprime.exceptions.JPClassNotFoundException;
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
import org.springframework.util.StringUtils;
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
      String pluralCode = cls.getPluralCode();
      if (StringUtils.hasText(pluralCode)) {
        cache.pluralCodeJpClassMap.put(pluralCode, cls);
      }
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
          String pluralCode = cls.getPluralCode();
          if (StringUtils.hasText(pluralCode)) {
            newCache.pluralCodeJpClassMap.put(pluralCode, cls);
          }
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
        String pluralCode = cls.getPluralCode();
        if (StringUtils.hasText(pluralCode)) {
          newCache.pluralCodeJpClassMap.put(pluralCode, cls);
        }
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

  @Override
  public JPClass getJPClassByCodeOrThrow(String code) {
    if (!StringUtils.hasText(code)) {
      throw new JPClassNotFoundException();
    }
    JPClass jpClass = cacheRef.get().codeJpClassMap.get(code);
    if (jpClass == null) {
      throw new JPClassNotFoundException(code);
    }
    return jpClass;
  }

  /**
   * Возвращает метаописание класса по множественному коду
   *
   * @param pluralCode Множественный код класса
   * @return Метаописание класса
   * @deprecated Отказ от множественного кодового имени. Рекомендуется использовать обычное кодовое имя
   */
  @Deprecated
  @Override
  public JPClass getJPClassByPluralCode(String pluralCode) {
    return pluralCode == null ? null : cacheRef.get().pluralCodeJpClassMap.get(pluralCode);
  }

  /**
   * Возвращает метаописание класса по множественному или простому коду
   *
   * @param code Код класса
   * @return Метаописание класса
   * @deprecated Отказ от множественного кодового имени. Рекомендуется использовать обычное кодовое имя
   */
  @Deprecated
  @Override
  public JPClass getJPClassByCodeOrPluralCode(String code) {
    if (code == null) {
      return null;
    }
    Cache cache = cacheRef.get();
    JPClass jpClass = cache.codeJpClassMap.get(code);
    if (jpClass == null) {
      jpClass = cache.pluralCodeJpClassMap.get(code);
    }
    return jpClass;
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
