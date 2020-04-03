package mp.jprime.security.services;

import mp.jprime.security.JPImmutableSecurityPackage;
import mp.jprime.security.JPSecurityDynamicLoader;
import mp.jprime.security.JPSecurityPackage;
import mp.jprime.security.annotations.services.JPSecurityAnnoLoader;
import mp.jprime.security.events.SecurityChangeEvent;
import mp.jprime.security.xmlloader.services.JPSecurityXmlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Описание настроек безопаности
 */
@Service
public final class JPSecurityMemoryStorage implements JPSecurityStorage {
  /**
   * Описания настроек безопаности
   */
  private Map<String, JPSecurityPackage> setts = new ConcurrentHashMap<>();
  /**
   * Динамическая загрузка настроек безопаности
   */
  private JPSecurityDynamicLoader dynamicLoader;

  /**
   * Считываем настройки доступа
   */
  private JPSecurityMemoryStorage(@Autowired JPSecurityAnnoLoader annoLoader,
                                  @Autowired JPSecurityXmlLoader xmlLoader,
                                  @Autowired(required = false) JPSecurityDynamicLoader dLoader) {
    this.dynamicLoader = dLoader;

    Collection<Flux<JPSecurityPackage>> p = new ArrayList<>();
    p.add(annoLoader.load());
    p.add(xmlLoader.load());
    Flux.concat(p)
        .filter(Objects::nonNull)
        .subscribe(this::applyJPPackageSecurity);
  }

  @Autowired(required = false)
  private void setDynamicLoader(JPSecurityDynamicLoader dynamicLoader) {
    this.dynamicLoader = dynamicLoader;
  }

  /**
   * Сохраняет указанную настройку
   *
   * @param sett настройка
   */
  private void applyJPPackageSecurity(JPSecurityPackage sett) {
    if (setts.containsKey(sett.getCode())) {
      return;
    }
    setts.put(sett.getCode(), sett);
  }

  /**
   * Возвращает загруженные настройки доступа
   *
   * @return Настройки доступа
   */
  @Override
  public Collection<JPSecurityPackage> getPackages() {
    return setts.values();
  }

  /**
   * Возвращает настройку доступа по коду
   *
   * @param code Код
   * @return Настройка доступа
   */
  @Override
  public JPSecurityPackage getJPPackageByCode(String code) {
    return setts.get(code);
  }

  /**
   * Проверка доступа на чтение
   *
   * @param packageCode Код пакета
   * @param roles       Роли
   * @return Да/Нет
   */
  @Override
  public boolean checkRead(String packageCode, Collection<String> roles) {
    JPSecurityPackage sett = packageCode != null ? setts.get(packageCode) : null;
    return sett == null || sett.checkRead(roles);
  }

  /**
   * Проверка доступа на удаление
   *
   * @param packageCode Код пакета
   * @param roles       Роли
   * @return Да/Нет
   */
  @Override
  public boolean checkDelete(String packageCode, Collection<String> roles) {
    JPSecurityPackage sett = packageCode != null ? setts.get(packageCode) : null;
    return sett == null || sett.checkDelete(roles);
  }

  /**
   * Проверка доступа на обновление
   *
   * @param packageCode Код пакета
   * @param roles       Роли
   * @return Да/Нет
   */
  @Override
  public boolean checkUpdate(String packageCode, Collection<String> roles) {
    JPSecurityPackage sett = packageCode != null ? setts.get(packageCode) : null;
    return sett == null || sett.checkUpdate(roles);
  }

  /**
   * Проверка доступа на создание
   *
   * @param packageCode Код пакета
   * @param roles       Роли
   * @return Да/Нет
   */
  @Override
  public boolean checkCreate(String packageCode, Collection<String> roles) {
    JPSecurityPackage sett = packageCode != null ? setts.get(packageCode) : null;
    return sett == null || sett.checkCreate(roles);
  }

  /**
   * Сохраняет указанный список настроек
   *
   * @param list Список настроек
   */
  private void applyDynamicJPSecurities(Collection<JPSecurityPackage> list) {
    if (list == null || list.isEmpty()) {
      return;
    }

    Map<String, JPSecurityPackage> setts = new ConcurrentHashMap<>();

    // Добавляем постоянные классы
    for (JPSecurityPackage sett : this.setts.values()) {
      if (sett instanceof JPImmutableSecurityPackage) {
        setts.put(sett.getCode(), sett);
      }
    }
    // Добавляем динамические классы
    for (JPSecurityPackage sett : list) {
      if (setts.containsKey(sett.getCode())) {
        continue;
      }
      setts.put(sett.getCode(), sett);
    }
    this.setts = setts;
  }

  @EventListener
  public void handleSecurityChangeEvent(SecurityChangeEvent event) {
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
          .subscribe(this::applyDynamicJPSecurities);
    }
  }
}
