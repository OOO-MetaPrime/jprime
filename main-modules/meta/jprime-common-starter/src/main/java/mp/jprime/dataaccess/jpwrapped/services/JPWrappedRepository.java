package mp.jprime.dataaccess.jpwrapped.services;

import mp.jprime.annotations.ClassesLink;
import mp.jprime.dataaccess.*;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.beans.JPUniqueValue;
import mp.jprime.dataaccess.enums.AnalyticFunction;
import mp.jprime.dataaccess.enums.BooleanCondition;
import mp.jprime.dataaccess.jpwrapped.repositories.JPWrappedStorage;
import mp.jprime.dataaccess.params.*;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.dataaccess.params.query.filters.BooleanFilter;
import mp.jprime.dataaccess.params.query.filters.DayFeatureFilter;
import mp.jprime.dataaccess.params.query.filters.FeatureFilter;
import mp.jprime.dataaccess.params.query.filters.PeriodFeatureFilter;
import mp.jprime.dataaccess.params.query.filters.attr.AttrValueFilter;
import mp.jprime.dataaccess.params.query.filters.attr.LinkFilter;
import mp.jprime.dataaccess.params.query.filters.value.CustomValueFilter;
import mp.jprime.dataaccess.uniquevalues.JPUniqueValuesRepository;
import mp.jprime.dataaccess.uniquevalues.JPUniqueValuesService;
import mp.jprime.exceptions.JPAttrMapNotFoundException;
import mp.jprime.exceptions.JPClassMapNotFoundException;
import mp.jprime.exceptions.JPClassNotFoundException;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.services.JPBeanService;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.metamaps.JPAttrMap;
import mp.jprime.metamaps.JPClassMap;
import mp.jprime.metamaps.services.JPMapsStorage;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.services.JPResourceAccess;
import mp.jprime.security.services.JPResourceAccessService;
import mp.jprime.security.services.JPSecurityStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
@ClassesLink(classes = {JPWrappedStorage.class})
public final class JPWrappedRepository implements JPObjectRepository, JPCUDValidator, JPSelectValidator, JPUniqueValuesRepository {
  // Логика работы с JPBean
  private final JPBeanService jpBeanService;
  // Хранилище метаинформации
  private final JPMetaStorage metaStorage;
  // Описания привязки метаинформации к хранилищу
  private final JPMapsStorage mapsStorage;

  private final JPResourceAccessService resourceAccessService;
  private final JPSecurityStorage securityManager;
  private final JPObjectRepositoryService repo;
  private final JPObjectAccessService objectAccessService;
  private final JPUniqueValuesService uniqueValuesService;

  private JPWrappedRepository(@Autowired JPBeanService jpBeanService,
                              @Autowired JPMetaStorage metaStorage,
                              @Autowired JPMapsStorage mapsStorage,
                              @Autowired JPResourceAccessService resourceAccessService,
                              @Autowired JPSecurityStorage securityManager,
                              @Autowired JPObjectRepositoryService repo,
                              @Autowired JPObjectAccessService objectAccessService,
                              @Autowired JPUniqueValuesService uniqueValuesService) {
    this.jpBeanService = jpBeanService;
    this.metaStorage = metaStorage;
    this.mapsStorage = mapsStorage;
    this.resourceAccessService = resourceAccessService;
    this.securityManager = securityManager;
    this.repo = repo;
    this.objectAccessService = objectAccessService;
    this.uniqueValuesService = uniqueValuesService;
  }

  private JPObjectRepositoryService getRepo() {
    return repo;
  }

  private JPUniqueValuesService getUniqueValuesService() {
    return uniqueValuesService;
  }

  @Override
  public JPObjectAccessService getJPObjectAccessService() {
    return objectAccessService;
  }

  @Override
  public JPSecurityStorage getSecurityStorage() {
    return securityManager;
  }

  @Override
  public JPResourceAccessService getJPResourceAccessService() {
    return resourceAccessService;
  }

  @Override
  public JPObject getObject(JPSelect query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return convertFrom(classInfo, getRepo().getObject(validateAndConvertTo(classInfo, query)), query.getAuth(), query.getSource());
  }

  @Override
  public JPObject getObjectAndLock(JPSelect query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return convertFrom(classInfo, getRepo().getObjectAndLock(validateAndConvertTo(classInfo, query)), query.getAuth(), query.getSource());
  }

  @Override
  public JPObject getObjectAndLock(JPSelect query, boolean skipLocked) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return convertFrom(classInfo, getRepo().getObjectAndLock(validateAndConvertTo(classInfo, query), skipLocked), query.getAuth(), query.getSource());
  }

  @Override
  public Optional<JPObject> getOptionalObject(JPSelect query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return getRepo().getOptionalObject(validateAndConvertTo(classInfo, query))
        .map(o -> convertFrom(classInfo, o, query.getAuth(), query.getSource()));
  }

  @Override
  public Mono<Long> getAsyncTotalCount(JPSelect query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return getRepo().getAsyncTotalCount(validateAndConvertTo(classInfo, query));
  }

  @Override
  public Long getTotalCount(JPSelect query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return getRepo().getTotalCount(validateAndConvertTo(classInfo, query));
  }

  @Override
  public Collection<JPObject> getList(JPSelect query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return convertFrom(classInfo, getRepo().getList(validateAndConvertTo(classInfo, query)), query.getAuth(), query.getSource());
  }

  @Override
  public Collection<JPObject> getListAndLock(JPSelect query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return convertFrom(classInfo, getRepo().getListAndLock(validateAndConvertTo(classInfo, query)), query.getAuth(), query.getSource());
  }

  @Override
  public Collection<JPObject> getListAndLock(JPSelect query, boolean skipLocked) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return convertFrom(classInfo, getRepo().getListAndLock(validateAndConvertTo(classInfo, query), skipLocked), query.getAuth(), query.getSource());
  }

  @Override
  public Mono<JPData> getAsyncAggregate(JPAggregate aggr) {
    ClassInfo classInfo = getClassInfo(aggr.getJpClass());
    return getRepo().getAsyncAggregate(validateAndConvertTo(classInfo, aggr))
        .map(data -> convertFrom(classInfo, data));
  }

  @Override
  public JPData getAggregate(JPAggregate aggr) {
    ClassInfo classInfo = getClassInfo(aggr.getJpClass());
    return convertFrom(classInfo, getRepo().getAggregate(validateAndConvertTo(classInfo, aggr)));
  }

  @Override
  public Mono<JPId> asyncCreate(JPCreate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return getRepo().asyncCreate(validateAndConvertTo(classInfo, query))
        .map(id -> convertFrom(classInfo, id));
  }

  @Override
  public JPId create(JPCreate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return convertFrom(classInfo, getRepo().create(validateAndConvertTo(classInfo, query)));
  }

  @Override
  public Mono<JPObject> asyncCreateAndGet(JPCreate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return getRepo().asyncCreateAndGet(validateAndConvertTo(classInfo, query))
        .map(o -> convertFrom(classInfo, o, query.getAuth(), query.getSource()));
  }

  @Override
  public JPObject createAndGet(JPCreate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return convertFrom(classInfo, getRepo().createAndGet(validateAndConvertTo(classInfo, query)), query.getAuth(), query.getSource());
  }

  @Override
  public Mono<JPId> asyncUpdate(JPUpdate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return getRepo().asyncUpdate(validateAndConvertTo(classInfo, query))
        .map(id -> convertFrom(classInfo, id));
  }

  @Override
  public Mono<Long> asyncUpdate(JPConditionalUpdate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return getRepo().asyncUpdate(validateAndConvertTo(classInfo, query));
  }

  @Override
  public JPId update(JPUpdate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return convertFrom(classInfo, getRepo().update(validateAndConvertTo(classInfo, query)));
  }

  @Override
  public Long update(JPConditionalUpdate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return getRepo().update(validateAndConvertTo(classInfo, query));
  }

  @Override
  public Mono<JPObject> asyncUpdateAndGet(JPUpdate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return getRepo().asyncUpdateAndGet(validateAndConvertTo(classInfo, query))
        .map(o -> convertFrom(classInfo, o, query.getAuth(), query.getSource()));
  }

  @Override
  public JPObject updateAndGet(JPUpdate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return convertFrom(classInfo, getRepo().updateAndGet(validateAndConvertTo(classInfo, query)), query.getAuth(), query.getSource());
  }

  @Override
  public Mono<JPId> asyncPatch(JPCreate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return getRepo().asyncPatch(validateAndConvertTo(classInfo, query))
        .map(id -> convertFrom(classInfo, id));
  }

  @Override
  public JPId patch(JPCreate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return convertFrom(classInfo, getRepo().patch(validateAndConvertTo(classInfo, query)));
  }

  @Override
  public Mono<JPObject> asyncPatchAndGet(JPCreate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return getRepo().asyncPatchAndGet(validateAndConvertTo(classInfo, query))
        .map(o -> convertFrom(classInfo, o, query.getAuth(), query.getSource()));
  }

  @Override
  public JPObject patchAndGet(JPCreate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return convertFrom(classInfo, getRepo().patchAndGet(validateAndConvertTo(classInfo, query)), query.getAuth(), query.getSource());
  }

  @Override
  public Mono<Long> asyncDelete(JPDelete query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return getRepo().asyncDelete(validateAndConvertTo(classInfo, query));
  }

  @Override
  public Long delete(JPDelete query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return getRepo().delete(validateAndConvertTo(classInfo, query));
  }

  @Override
  public Mono<Long> asyncDelete(JPConditionalDelete query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return getRepo().asyncDelete(validateAndConvertTo(classInfo, query));
  }

  @Override
  public Long delete(JPConditionalDelete query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return getRepo().delete(validateAndConvertTo(classInfo, query));
  }

  @Override
  public Mono<Void> asyncBatch(JPBatchCreate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return getRepo().asyncBatch(validateAndConvertTo(classInfo, query));
  }

  @Override
  public void batch(JPBatchCreate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    getRepo().batch(validateAndConvertTo(classInfo, query));
  }

  @Override
  public <T> List<T> batchWithKeys(JPBatchCreate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return getRepo().batchWithKeys(validateAndConvertTo(classInfo, query));
  }

  @Override
  public Mono<Void> asyncBatch(JPBatchUpdate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return getRepo().asyncBatch(validateAndConvertTo(classInfo, query));
  }

  @Override
  public void batch(JPBatchUpdate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    getRepo().batch(validateAndConvertTo(classInfo, query));
  }

  @Override
  public Mono<JPObject> getAsyncObject(JPSelect query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return getRepo().getAsyncObject(validateAndConvertTo(classInfo, query))
        .map(o -> convertFrom(classInfo, o, query.getAuth(), query.getSource()));
  }

  @Override
  public Mono<JPObject> getAsyncObjectAndLock(JPSelect query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return getRepo().getAsyncObjectAndLock(validateAndConvertTo(classInfo, query))
        .map(o -> convertFrom(classInfo, o, query.getAuth(), query.getSource()));
  }

  @Override
  public Flux<JPObject> getAsyncList(JPSelect query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return getRepo().getAsyncList(validateAndConvertTo(classInfo, query))
        .map(o -> convertFrom(classInfo, o, query.getAuth(), query.getSource()));
  }

  @Override
  public Flux<JPObject> getAsyncListAndLock(JPSelect query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return getRepo().getAsyncListAndLock(validateAndConvertTo(classInfo, query))
        .map(o -> convertFrom(classInfo, o, query.getAuth(), query.getSource()));
  }

  @Override
  public Collection<JPUniqueValue> getUniqueValues(JPSelect select, List<String> hierarchy) {
    ClassInfo classInfo = getClassInfo(select.getJpClass());

    return convertFromUniqueValue(classInfo,
        getUniqueValuesService().getUniqueValues(validateAndConvertTo(classInfo, select), hierarchy),
        select.getAuth(), select.getSource());
  }

  private JPAggregate validateAndConvertTo(ClassInfo classInfo, JPAggregate query) {
    String wrapJpClass = classInfo.wrapJpClass();

    JPResourceAccess access = validate(query);

    JPAggregate.Builder builder = JPAggregate.from(wrapJpClass)
        .auth(query.getAuth())
        .source(Source.SYSTEM)
        .where(convertTo(classInfo, getFilter(query.getWhere(), access)))
        .timeout(query.getTimeout());

    query.getAggrs().forEach(a -> builder.aggr(a.getAlias(), a.getAttr(), a.getOperator()));

    return builder.build();
  }

  private JPSelect validateAndConvertTo(ClassInfo classInfo, JPSelect query) {
    String wrapJpClass = classInfo.wrapJpClass();

    JPResourceAccess access = validate(query);

    JPSelect.Builder builder = JPSelect.from(wrapJpClass)
        .auth(query.getAuth())
        .source(Source.SYSTEM)
        .offset(query.getOffset())
        .limit(query.getLimit())
        .totalCount(query.isTotalCount())
        .useDefaultJpAttrs(query.isUseDefaultJpAttrs())
        .where(convertTo(classInfo, getFilter(query.getWhere(), access)))
        .timeout(query.getTimeout());

    query.getAttrs().forEach(code -> {
      Collection<String> linked = query.getLinkAttrs(code);
      String attr = classInfo.wrapJpAttr(code);
      if (linked != null) {
        builder.attr(attr, linked);
      } else {
        builder.attr(attr);
      }
    });

    query.getOrderBy().forEach(o -> builder.orderBy(classInfo.wrapJpAttr(o.getAttr()), o.getOrder()));

    return builder.build();
  }

  private Filter getFilter(Filter selectWhere, JPResourceAccess access) {
    Filter filter = access == null ? null : access.getFilter();
    if (filter == null) {
      filter = selectWhere;
    } else if (selectWhere != null) {
      filter = Filter.and(filter, selectWhere);
    }
    return filter;
  }

  private JPCreate validateAndConvertTo(ClassInfo classInfo, JPCreate query) {
    String wrapJpClass = classInfo.wrapJpClass();

    validate(classInfo.jpClass(), query);

    JPCreate.Builder builder = JPCreate.create(wrapJpClass).auth(query.getAuth()).source(Source.SYSTEM);

    query.getData().forEach((code, value) -> builder.set(classInfo.wrapJpAttr(code), value));

    builder.onConflictDoNothing(query.isOnConflictDoNothing());
    if (query.isUpsert()) {
      Collection<String> conflictAttr = query.getConflictAttr().stream()
          .map(classInfo::wrapJpAttr)
          .toList();
      Collection<String> conflictSet = query.getConflictSet().stream()
          .map(classInfo::wrapJpAttr)
          .toList();
      builder.upsert(conflictAttr, conflictSet);
    }

    return builder.build();
  }

  private JPUpdate validateAndConvertTo(ClassInfo classInfo, JPUpdate query) {
    String wrapJpClass = classInfo.wrapJpClass();

    validate(classInfo.jpClass(), query);

    JPUpdate.Builder builder = JPUpdate.update(JPId.get(wrapJpClass, query.getJpId().getId()))
        .auth(query.getAuth())
        .source(Source.SYSTEM)
        .autoChangeDate(query.isAutoChangeDate())
        .where(convertTo(classInfo, query.getWhere()));

    query.getData().forEach((code, value) -> builder.set(classInfo.wrapJpAttr(code), value));

    return builder.build();
  }

  private JPConditionalUpdate validateAndConvertTo(ClassInfo classInfo, JPConditionalUpdate query) {
    String wrapJpClass = classInfo.wrapJpClass();

    validate(classInfo.jpClass(), query);

    JPConditionalUpdate.Builder builder = JPConditionalUpdate.update(wrapJpClass, convertTo(classInfo, query.getWhere()))
        .auth(query.getAuth())
        .source(Source.SYSTEM)
        .autoChangeDate(query.isAutoChangeDate());

    query.getData().forEach((code, value) -> builder.set(classInfo.wrapJpAttr(code), value));

    return builder.build();
  }

  private JPBatchCreate validateAndConvertTo(ClassInfo classInfo, JPBatchCreate query) {
    validate(query);

    JPBatchCreate.Builder builder = JPBatchCreate.create(classInfo.wrapJpClass());
    query.getData().forEach(data -> {
      data.forEach((code, value) -> builder.set(classInfo.wrapJpAttr(code), value));
      builder.addBatch();
    });
    builder.onConflictDoNothing(query.isOnConflictDoNothing());
    if (query.isUpsert()) {
      Collection<String> conflictAttr = query.getConflictAttr().stream()
          .map(classInfo::wrapJpAttr)
          .toList();
      Collection<String> conflictSet = query.getConflictSet().stream()
          .map(classInfo::wrapJpAttr)
          .toList();
      builder.upsert(conflictAttr, conflictSet);
    }

    return builder.build();
  }

  private JPBatchUpdate validateAndConvertTo(ClassInfo classInfo, JPBatchUpdate query) {
    validate(classInfo.jpClass(), query);

    JPBatchUpdate.Builder builder = JPBatchUpdate.update(classInfo.wrapJpClass());
    query.forEach((id, attrs) -> {
      builder.id(id);
      attrs.forEach((code, value) -> builder.set(classInfo.wrapJpAttr(code), value));
      builder.addBatch();
    });

    return builder.build();
  }

  private JPDelete validateAndConvertTo(ClassInfo classInfo, JPDelete query) {
    String wrapJpClass = classInfo.wrapJpClass();

    validate(query);

    return JPDelete.delete(JPId.get(wrapJpClass, query.getJpId().getId())).auth(query.getAuth()).source(Source.SYSTEM).build();
  }

  private JPConditionalDelete validateAndConvertTo(ClassInfo classInfo, JPConditionalDelete query) {
    String wrapJpClass = classInfo.wrapJpClass();

    validate(query);

    return JPConditionalDelete.delete(wrapJpClass, convertTo(classInfo, query.getWhere())).auth(query.getAuth()).source(Source.SYSTEM).build();
  }

  private JPObject convertFrom(ClassInfo classInfo, JPObject object, AuthInfo auth, Source source) {
    if (object == null) {
      return null;
    }
    JPData data = object.getData();
    Map<String, Object> newData = new HashMap<>(data.size());
    classInfo.jpClassMap().getAttrs().forEach(map -> {
      String attrCode = map.getCode();
      JPAttr jpAttr = classInfo.jpClass().getAttr(attrCode);
      if (jpAttr == null) {
        return;
      }
      String mapCode = map.getMap();

      if (data.containsKey(mapCode)) {
        if (auth != null && source == Source.USER) {
          if (!securityManager.checkRead(jpAttr.getJpPackage(), auth.getRoles())) {
            return;
          }
        }
        newData.put(attrCode, data.get(mapCode));
      }
    });
    return jpBeanService.newInstance(classInfo.jpClass(), JPData.of(newData));
  }

  private JPUniqueValue convertFromUniqueValue(ClassInfo classInfo, JPUniqueValue object, AuthInfo auth, Source source) {
    if (object == null) {
      return null;
    }
    for (JPAttrMap map : classInfo.jpClassMap().getAttrs()) {
      String attrCode = map.getCode();
      JPAttr jpAttr = classInfo.jpClass().getAttr(attrCode);
      if (jpAttr == null) {
        continue;
      }
      String mapCode = map.getMap();

      if (object.getAttr().equals(mapCode)) {
        if (auth != null && source == Source.USER) {
          if (!securityManager.checkRead(jpAttr.getJpPackage(), auth.getRoles())) {
            return null;
          }
        }
        return JPUniqueValue.of(
            attrCode,
            object.getValue(),
            convertFromUniqueValue(classInfo, object.getSubValues(), auth, source)
        );
      }
    }
    return null;
  }

  private JPId convertFrom(ClassInfo classInfo, JPId jpId) {
    return JPId.get(classInfo.jpClass().getName(), jpId.getId());
  }

  private JPData convertFrom(ClassInfo classInfo, JPData data) {
    if (data == null || data.isEmpty()) {
      return data;
    }
    Map<String, Object> newData = new HashMap<>(data.size());
    classInfo.jpClassMap().getAttrs().forEach(map -> {
      String attrCode = map.getCode();
      String mapCode = map.getMap();

      if (data.containsKey(mapCode)) {
        newData.put(attrCode, data.get(mapCode));
      }
    });
    return JPData.of(newData);
  }

  private Collection<JPObject> convertFrom(ClassInfo classInfo, Collection<JPObject> list, AuthInfo auth, Source source) {
    if (list == null || list.isEmpty()) {
      return list;
    }
    List<JPObject> result = new ArrayList<>(list.size());
    list.forEach(object -> result.add(convertFrom(classInfo, object, auth, source)));
    return result;
  }

  private Collection<JPUniqueValue> convertFromUniqueValue(ClassInfo classInfo, Collection<JPUniqueValue> list, AuthInfo auth, Source source) {
    if (list == null || list.isEmpty()) {
      return list;
    }
    List<JPUniqueValue> result = new ArrayList<>(list.size());
    list.forEach(object -> result.add(convertFromUniqueValue(classInfo, object, auth, source)));
    return result;
  }

  private ClassInfo getClassInfo(String classCode) {
    JPClass jpClass = metaStorage.getJPClassByCode(classCode);
    if (jpClass == null) {
      throw new JPClassNotFoundException(classCode);
    }
    // Получаем маппинг класса
    JPClassMap jpClassMap = mapsStorage.get(jpClass);
    if (jpClassMap == null) {
      throw new JPClassMapNotFoundException(classCode);
    }
    return new ClassInfo(jpClass, jpClassMap);
  }

  private record ClassInfo(JPClass jpClass, JPClassMap jpClassMap) {
    String wrapJpClass() {
      return jpClassMap.getMap();
    }

    String wrapJpAttr(String attr) {
      JPAttrMap attrMap = jpClassMap.getAttr(attr);
      String result = attrMap != null ? attrMap.getMap() : null;
      if (result == null) {
        throw new JPAttrMapNotFoundException(jpClass.getCode(), attr);
      }
      return result;
    }
  }

  private Filter convertTo(ClassInfo classInfo, Filter filter) {
    if (filter == null) {
      return null;
    }
    if (filter instanceof BooleanFilter c) {
      if (c.getCond() == BooleanCondition.AND) {
        return Filter.and(
            c.getFilters().stream()
                .map(f -> convertTo(classInfo, f))
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
        );
      } else if (c.getCond() == BooleanCondition.OR) {
        return Filter.or(
            c.getFilters().stream()
                .map(f -> convertTo(classInfo, f))
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
        );
      }
    }


    Filter result = null;
    if (filter instanceof PeriodFeatureFilter v) {
      result = v;
    } else if (filter instanceof DayFeatureFilter v) {
      result = v;
    } else if (filter instanceof FeatureFilter v) {
      result = v;
    } else if (filter instanceof CustomValueFilter v) {
      result = v;
    } else if (filter instanceof AttrValueFilter v) {
      String attrName = classInfo.wrapJpAttr(v.getAttrCode());
      result = v.ofAttr(attrName);
    } else if (filter instanceof LinkFilter l) {
      String attrName = classInfo.wrapJpAttr(l.getAttrCode());
      if (l.getFunction() == AnalyticFunction.EXISTS) {
        result = Filter.attr(attrName).exists(l.getFilter());
      } else if (l.getFunction() == AnalyticFunction.NOTEXISTS) {
        result = Filter.attr(attrName).notExists(l.getFilter());
      }
    }
    return result;
  }
}