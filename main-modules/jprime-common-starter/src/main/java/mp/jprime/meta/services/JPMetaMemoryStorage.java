package mp.jprime.meta.services;

import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPImmutableClass;
import mp.jprime.meta.JPMetaDynamicLoader;
import mp.jprime.meta.annotations.services.JPMetaAnnoLoader;
import mp.jprime.meta.events.JPMetaChangeEvent;
import mp.jprime.meta.xmlloader.services.JPMetaXmlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Хранилище метаинформации
 */
@Service
public final class JPMetaMemoryStorage implements JPMetaStorage {
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

  /**
   * Динамическая загрузка меты
   */
  private JPMetaDynamicLoader dynamicLoader;

  /**
   * Размещает метаописание в хранилище
   */
  private JPMetaMemoryStorage(@Autowired JPMetaAnnoLoader annoLoader,
                              @Autowired JPMetaXmlLoader xmlLoader) {
    Collection<Flux<JPClass>> p = new ArrayList<>();
    p.add(annoLoader.load());
    p.add(xmlLoader.load());
    Flux.concat(p)
        .filter(Objects::nonNull)
        .subscribe(this::applyJPClass);
  }

  @Autowired(required = false)
  private void setDynamicLoader(JPMetaDynamicLoader dynamicLoader) {
    this.dynamicLoader = dynamicLoader;
  }

  /**
   * Сохраняет указанный класс
   *
   * @param cls Класс
   */
  private void applyJPClass(JPClass cls) {
    if (codeJpClassMap.containsKey(cls.getCode())) {
      return;
    }
    classes.add(cls);
    codeJpClassMap.put(cls.getCode(), cls);
    pluralCodeJpClassMap.put(cls.getPluralCode(), cls);
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

    Collection<JPClass> classes = ConcurrentHashMap.newKeySet();
    Collection<JPClass> umClasses = Collections.unmodifiableCollection(classes);
    Map<String, JPClass> codeJpClassMap = new ConcurrentHashMap<>();
    Map<String, JPClass> pluralCodeJpClassMap = new ConcurrentHashMap<>();
    // Добавляем постоянные настройки
    for (JPClass cls : this.classes) {
      if (cls instanceof JPImmutableClass) {
        classes.add(cls);
        codeJpClassMap.put(cls.getCode(), cls);
        pluralCodeJpClassMap.put(cls.getPluralCode(), cls);
      }
    }
    // Добавляем динамические настройки
    for (JPClass cls : list) {
      String code = cls.getCode();
      if (code == null || codeJpClassMap.containsKey(code)) {
        continue;
      }
      classes.add(cls);
      codeJpClassMap.put(code, cls);
      pluralCodeJpClassMap.put(cls.getPluralCode(), cls);
    }
    this.classes = classes;
    this.umClasses = umClasses;
    this.codeJpClassMap = codeJpClassMap;
    this.pluralCodeJpClassMap = pluralCodeJpClassMap;
  }


  /**
   * Возвращает метаописания классов
   *
   * @return метаописания классов
   */
  @Override
  public Collection<JPClass> getJPClasses() {
    return umClasses;
  }

  /**
   * Возвращает метаописание класса по коду
   *
   * @param code Код класса
   * @return Метаописание класса
   */
  @Override
  public JPClass getJPClassByCode(String code) {
    return code == null ? null : codeJpClassMap.get(code);
  }

  /**
   * Возвращает метаописание класса по коду
   *
   * @param pluralCode Множественный код класса
   * @return Метаописание класса
   */
  @Override
  public JPClass getJPClassByPluralCode(String pluralCode) {
    return pluralCode == null ? null : pluralCodeJpClassMap.get(pluralCode);
  }

  @EventListener
  public void handleJPMetaChangeEvent(JPMetaChangeEvent event) {
    dynamicLoad();
  }

  @EventListener
  public void handleContextRefreshedEvent(ContextRefreshedEvent event) {
    dynamicLoad();
  }

  private void dynamicLoad() {
    if (dynamicLoader != null) {
      dynamicLoader.load()
          .collectList()
          .subscribe(this::applyDynamicJPClasses);
    }
  }
}
