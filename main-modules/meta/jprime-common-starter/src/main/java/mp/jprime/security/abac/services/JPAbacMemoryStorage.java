package mp.jprime.security.abac.services;

import mp.jprime.dataaccess.JPAction;
import mp.jprime.events.systemevents.JPSystemApplicationEvent;
import mp.jprime.security.abac.JPAbacDynamicLoader;
import mp.jprime.security.abac.Policy;
import mp.jprime.security.abac.PolicySet;
import mp.jprime.security.abac.PolicyTarget;
import mp.jprime.security.abac.annotations.JPAbacAnnoLoader;
import mp.jprime.security.abac.xmlloader.services.JPAbacXmlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Описание настроек ABAC
 */
@Service
@Lazy(value = false)
public final class JPAbacMemoryStorage implements JPAbacStorage {
  /**
   * Описания настроек ABAC
   */
  private Collection<PolicySet> allSetts = new CopyOnWriteArrayList<>();
  private ActionCache commonPolicies = new ActionCache();
  private Map<String, ActionCache> classPolicies = new ConcurrentHashMap<>();
  /**
   * Динамическая загрузка настроек ABAC
   */
  private Collection<JPAbacDynamicLoader> dynamicLoaders;

  /**
   * Считываем настройки доступа
   */
  private JPAbacMemoryStorage(@Autowired JPAbacAnnoLoader annoLoader,
                              @Autowired JPAbacXmlLoader xmlLoader) {
    Collection<Flux<PolicySet>> p = new ArrayList<>();
    p.add(annoLoader.load());
    p.add(xmlLoader.load());
    Flux.concat(p)
        .filter(Objects::nonNull)
        .subscribe(this::applyJPAbac);
  }

  @Autowired(required = false)
  private void setDynamicLoaders(Collection<JPAbacDynamicLoader> dLoaders) {
    this.dynamicLoaders = dLoaders != null ? dLoaders : Collections.emptyList();
  }

  /**
   * Сохраняет указанную настройку
   *
   * @param sett настройка
   */
  private void applyJPAbac(PolicySet sett) {
    applyJPAbac(allSetts, commonPolicies, classPolicies, sett);
  }

  /**
   * Сохраняет указанную настройку
   *
   * @param sett настройка
   */
  private void applyJPAbac(Collection<PolicySet> allSetts,
                           ActionCache commonPolicies,
                           Map<String, ActionCache> classPolicies,
                           PolicySet sett) {
    allSetts.add(sett);
    PolicyTarget target = sett.getTarget();
    if (target == null || target.getJpClassCodes().isEmpty()) {
      commonPolicies.addAll(sett.getPolicies());
    } else {
      target.getJpClassCodes().forEach(
          x -> classPolicies.computeIfAbsent(x, v -> new ActionCache()).addAll(sett.getPolicies())
      );
    }
  }

  /**
   * Возвращает загруженные настройки ABAC
   *
   * @return Настройки ABAC
   */
  @Override
  public Collection<PolicySet> getSettings() {
    return Collections.unmodifiableCollection(allSetts);
  }

  /**
   * Возвращает загруженные настройки ABAC для указанного класса по указанному действию
   *
   * @param jpClass Кодовое имя класс
   * @param action  Действие
   * @return Настройки ABAC для указанного класса
   */
  @Override
  public Collection<Policy> getSettings(String jpClass, JPAction action) {
    ActionCache actionCache = classPolicies.get(jpClass);
    Collection<Policy> policies = actionCache == null ? null : actionCache.get(action);
    return Collections.unmodifiableCollection(policies == null ? Collections.emptyList() : policies);
  }

  /**
   * Сохраняет указанный список настроек
   *
   * @param list Список настроек
   */
  private void applyDynamicAbac(Collection<PolicySet> list) {
    if (list == null || list.isEmpty()) {
      return;
    }

    Collection<PolicySet> allSetts = new CopyOnWriteArrayList<>();
    ActionCache commonPolicies = new ActionCache();
    Map<String, ActionCache> classPolicies = new ConcurrentHashMap<>();

    // Добавляем постоянные настройки
    for (PolicySet sett : this.allSetts) {
      if (sett.isImmutable()) {
        applyJPAbac(allSetts, commonPolicies, classPolicies, sett);
      }
    }
    // Добавляем динамические настройки
    for (PolicySet sett : list) {
      applyJPAbac(allSetts, commonPolicies, classPolicies, sett);
    }
    this.allSetts = allSetts;
    this.commonPolicies = commonPolicies;
    this.classPolicies = classPolicies;
  }

  @EventListener(condition = "#event.eventCode.equals(T(mp.jprime.security.abac.events.AbacChangeEvent).CODE)")
  public void handleAbacChangeEvent(JPSystemApplicationEvent event) {
    dynamicLoad();
  }

  @EventListener
  public void handleContextRefreshedEvent(ContextRefreshedEvent event) {
    dynamicLoad();
  }

  private void dynamicLoad() {
    if (dynamicLoaders == null || dynamicLoaders.isEmpty()) {
      return;
    }
    Flux
        .concat(
            dynamicLoaders.stream()
                .map(JPAbacDynamicLoader::load)
                .collect(Collectors.toList())
        )
        .filter(Objects::nonNull)
        .collectList()
        .subscribe(this::applyDynamicAbac);
  }

  private class ActionCache {
    private Map<JPAction, Collection<Policy>> actionPolicies = new ConcurrentHashMap<>();

    private ActionCache() {

    }

    private Collection<Policy> get(JPAction action) {
      return action == null ? null : actionPolicies.get(action);
    }

    private void add(Policy policy) {
      policy.getActions().forEach(
          x -> actionPolicies.computeIfAbsent(x, v -> new CopyOnWriteArrayList<>()).add(policy)
      );
    }

    private void addAll(Collection<Policy> policies) {
      policies.forEach(this::add);
    }
  }
}
