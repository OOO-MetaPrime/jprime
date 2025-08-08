package mp.jprime.dataaccess.jpwrapped.services;

import mp.jprime.annotations.ClassesLink;
import mp.jprime.dataaccess.*;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.enums.AnalyticFunction;
import mp.jprime.dataaccess.enums.BooleanCondition;
import mp.jprime.dataaccess.enums.FilterOperation;
import mp.jprime.dataaccess.jpwrapped.repositories.JPWrappedStorage;
import mp.jprime.dataaccess.params.*;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.dataaccess.params.query.filters.*;
import mp.jprime.dataaccess.params.query.filters.range.*;
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
import mp.jprime.security.services.JPResourceAccessServiceAware;
import mp.jprime.security.services.JPSecurityStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
@ClassesLink(classes = {JPWrappedStorage.class})
public final class JPWrappedRepository implements JPObjectRepository, JPCUDValidator, JPSelectValidator, JPObjectAccessServiceAware, JPObjectRepositoryServiceAware, JPResourceAccessServiceAware {
  // Логика работы с JPBean
  private JPBeanService jpBeanService;
  // Хранилище метаинформации
  private JPMetaStorage metaStorage;
  // Описания привязки метаинформации к хранилищу
  private JPMapsStorage mapsStorage;

  private JPObjectAccessService objectAccessService;
  private JPObjectRepositoryService repositoryService;
  private JPResourceAccessService resourceAccessService;
  private JPSecurityStorage securityManager;

  @Autowired
  private void setJpBeanService(JPBeanService jpBeanService) {
    this.jpBeanService = jpBeanService;
  }

  @Autowired
  private void setMetaStorage(JPMetaStorage metaStorage) {
    this.metaStorage = metaStorage;
  }

  @Autowired
  private void setMapsStorage(JPMapsStorage mapsStorage) {
    this.mapsStorage = mapsStorage;
  }

  @Override
  public void setJpObjectAccessService(JPObjectAccessService objectAccessService) {
    this.objectAccessService = objectAccessService;
  }

  @Override
  public void setJpObjectRepositoryService(JPObjectRepositoryService repositoryService) {
    this.repositoryService = repositoryService;
  }

  @Override
  public void setJpResourceAccessService(JPResourceAccessService resourceAccessService) {
    this.resourceAccessService = resourceAccessService;
  }

  @Autowired
  private void setSecurityManager(JPSecurityStorage securityManager) {
    this.securityManager = securityManager;
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
    return convertFrom(classInfo, repositoryService.getObject(validateAndConvertTo(classInfo, query)), query.getAuth(), query.getSource());
  }

  @Override
  public JPObject getObjectAndLock(JPSelect query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return convertFrom(classInfo, repositoryService.getObjectAndLock(validateAndConvertTo(classInfo, query)), query.getAuth(), query.getSource());
  }

  @Override
  public JPObject getObjectAndLock(JPSelect query, boolean skipLocked) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return convertFrom(classInfo, repositoryService.getObjectAndLock(validateAndConvertTo(classInfo, query), skipLocked), query.getAuth(), query.getSource());
  }

  @Override
  public Optional<JPObject> getOptionalObject(JPSelect query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return repositoryService.getOptionalObject(validateAndConvertTo(classInfo, query))
        .map(o -> convertFrom(classInfo, o, query.getAuth(), query.getSource()));
  }

  @Override
  public Mono<Long> getAsyncTotalCount(JPSelect query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return repositoryService.getAsyncTotalCount(validateAndConvertTo(classInfo, query));
  }

  @Override
  public Long getTotalCount(JPSelect query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return repositoryService.getTotalCount(validateAndConvertTo(classInfo, query));
  }

  @Override
  public Collection<JPObject> getList(JPSelect query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return convertFrom(classInfo, repositoryService.getList(validateAndConvertTo(classInfo, query)), query.getAuth(), query.getSource());
  }

  @Override
  public Collection<JPObject> getListAndLock(JPSelect query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return convertFrom(classInfo, repositoryService.getListAndLock(validateAndConvertTo(classInfo, query)), query.getAuth(), query.getSource());
  }

  @Override
  public Collection<JPObject> getListAndLock(JPSelect query, boolean skipLocked) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return convertFrom(classInfo, repositoryService.getListAndLock(validateAndConvertTo(classInfo, query), skipLocked), query.getAuth(), query.getSource());
  }

  @Override
  public Mono<JPData> getAsyncAggregate(JPAggregate aggr) {
    ClassInfo classInfo = getClassInfo(aggr.getJpClass());
    return repositoryService.getAsyncAggregate(validateAndConvertTo(classInfo, aggr))
        .map(data -> convertFrom(classInfo, data));
  }

  @Override
  public JPData getAggregate(JPAggregate aggr) {
    ClassInfo classInfo = getClassInfo(aggr.getJpClass());
    return convertFrom(classInfo, repositoryService.getAggregate(validateAndConvertTo(classInfo, aggr)));
  }

  @Override
  public Mono<JPId> asyncCreate(JPCreate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return repositoryService.asyncCreate(validateAndConvertTo(classInfo, query))
        .map(id -> convertFrom(classInfo, id));
  }

  @Override
  public JPId create(JPCreate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return convertFrom(classInfo, repositoryService.create(validateAndConvertTo(classInfo, query)));
  }

  @Override
  public Mono<JPObject> asyncCreateAndGet(JPCreate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return repositoryService.asyncCreateAndGet(validateAndConvertTo(classInfo, query))
        .map(o -> convertFrom(classInfo, o, query.getAuth(), query.getSource()));
  }

  @Override
  public JPObject createAndGet(JPCreate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return convertFrom(classInfo, repositoryService.createAndGet(validateAndConvertTo(classInfo, query)), query.getAuth(), query.getSource());
  }

  @Override
  public Mono<JPId> asyncUpdate(JPUpdate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return repositoryService.asyncUpdate(validateAndConvertTo(classInfo, query))
        .map(id -> convertFrom(classInfo, id));
  }

  @Override
  public Mono<Long> asyncUpdate(JPConditionalUpdate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return repositoryService.asyncUpdate(validateAndConvertTo(classInfo, query));
  }

  @Override
  public JPId update(JPUpdate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return convertFrom(classInfo, repositoryService.update(validateAndConvertTo(classInfo, query)));
  }

  @Override
  public Long update(JPConditionalUpdate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return repositoryService.update(validateAndConvertTo(classInfo, query));
  }

  @Override
  public Mono<JPObject> asyncUpdateAndGet(JPUpdate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return repositoryService.asyncUpdateAndGet(validateAndConvertTo(classInfo, query))
        .map(o -> convertFrom(classInfo, o, query.getAuth(), query.getSource()));
  }

  @Override
  public JPObject updateAndGet(JPUpdate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return convertFrom(classInfo, repositoryService.updateAndGet(validateAndConvertTo(classInfo, query)), query.getAuth(), query.getSource());
  }

  @Override
  public Mono<JPId> asyncPatch(JPCreate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return repositoryService.asyncPatch(validateAndConvertTo(classInfo, query))
        .map(id -> convertFrom(classInfo, id));
  }

  @Override
  public JPId patch(JPCreate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return convertFrom(classInfo, repositoryService.patch(validateAndConvertTo(classInfo, query)));
  }

  @Override
  public Mono<JPObject> asyncPatchAndGet(JPCreate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return repositoryService.asyncPatchAndGet(validateAndConvertTo(classInfo, query))
        .map(o -> convertFrom(classInfo, o, query.getAuth(), query.getSource()));
  }

  @Override
  public JPObject patchAndGet(JPCreate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return convertFrom(classInfo, repositoryService.patchAndGet(validateAndConvertTo(classInfo, query)), query.getAuth(), query.getSource());
  }

  @Override
  public Mono<Long> asyncDelete(JPDelete query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return repositoryService.asyncDelete(validateAndConvertTo(classInfo, query));
  }

  @Override
  public Long delete(JPDelete query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return repositoryService.delete(validateAndConvertTo(classInfo, query));
  }

  @Override
  public Mono<Long> asyncDelete(JPConditionalDelete query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return repositoryService.asyncDelete(validateAndConvertTo(classInfo, query));
  }

  @Override
  public Long delete(JPConditionalDelete query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return repositoryService.delete(validateAndConvertTo(classInfo, query));
  }

  @Override
  public Mono<Void> asyncBatch(JPBatchCreate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return repositoryService.asyncBatch(validateAndConvertTo(classInfo, query));
  }

  @Override
  public void batch(JPBatchCreate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    repositoryService.batch(validateAndConvertTo(classInfo, query));
  }

  @Override
  public <T> List<T> batchWithKeys(JPBatchCreate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return repositoryService.batchWithKeys(validateAndConvertTo(classInfo, query));
  }

  @Override
  public Mono<Void> asyncBatch(JPBatchUpdate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return repositoryService.asyncBatch(validateAndConvertTo(classInfo, query));
  }

  @Override
  public void batch(JPBatchUpdate query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    repositoryService.batch(validateAndConvertTo(classInfo, query));
  }

  @Override
  public Mono<JPObject> getAsyncObject(JPSelect query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return repositoryService.getAsyncObject(validateAndConvertTo(classInfo, query))
        .map(o -> convertFrom(classInfo, o, query.getAuth(), query.getSource()));
  }

  @Override
  public Mono<JPObject> getAsyncObjectAndLock(JPSelect query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return repositoryService.getAsyncObjectAndLock(validateAndConvertTo(classInfo, query))
        .map(o -> convertFrom(classInfo, o, query.getAuth(), query.getSource()));
  }

  @Override
  public Flux<JPObject> getAsyncList(JPSelect query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return repositoryService.getAsyncList(validateAndConvertTo(classInfo, query))
        .map(o -> convertFrom(classInfo, o, query.getAuth(), query.getSource()));
  }

  @Override
  public Flux<JPObject> getAsyncListAndLock(JPSelect query) {
    ClassInfo classInfo = getClassInfo(query.getJpClass());
    return repositoryService.getAsyncListAndLock(validateAndConvertTo(classInfo, query))
        .map(o -> convertFrom(classInfo, o, query.getAuth(), query.getSource()));
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
    if (filter instanceof PeriodFeatureFilter) {
      result = filter;
    } else if (filter instanceof DayFeatureFilter) {
      result = filter;
    } else if (filter instanceof FeatureFilter) {
      result = filter;
    } else if (filter instanceof YearFilter) {
      YearFilter v = (YearFilter) filter;
      String attrName = classInfo.wrapJpAttr(v.getAttrCode());
      if (v.getOper() == FilterOperation.EQ_YEAR) {
        result = Filter.attr(attrName).eqYear(v.getValue());
      } else if (v.getOper() == FilterOperation.GT_YEAR) {
        result = Filter.attr(attrName).gtYear(v.getValue());
      } else if (v.getOper() == FilterOperation.GTE_YEAR) {
        result = Filter.attr(attrName).gteYear(v.getValue());
      } else if (v.getOper() == FilterOperation.NEQ_YEAR) {
        result = Filter.attr(attrName).neqYear(v.getValue());
      } else if (v.getOper() == FilterOperation.LT_YEAR) {
        result = Filter.attr(attrName).ltYear(v.getValue());
      } else if (v.getOper() == FilterOperation.LTE_YEAR) {
        result = Filter.attr(attrName).lteYear(v.getValue());
      }
    } else if (filter instanceof LocalDateDateFilter v) {
      String attrName = classInfo.wrapJpAttr(v.getAttrCode());
      if (v.getOper() == FilterOperation.EQ_DAY) {
        result = Filter.attr(attrName).eqDay(v.getValue());
      } else if (v.getOper() == FilterOperation.GT_DAY) {
        result = Filter.attr(attrName).gtDay(v.getValue());
      } else if (v.getOper() == FilterOperation.GTE_DAY) {
        result = Filter.attr(attrName).gteDay(v.getValue());
      } else if (v.getOper() == FilterOperation.NEQ_DAY) {
        result = Filter.attr(attrName).neqDay(v.getValue());
      } else if (v.getOper() == FilterOperation.LT_DAY) {
        result = Filter.attr(attrName).ltDay(v.getValue());
      } else if (v.getOper() == FilterOperation.LTE_DAY) {
        result = Filter.attr(attrName).lteDay(v.getValue());
      } else if (v.getOper() == FilterOperation.EQ_MONTH) {
        result = Filter.attr(attrName).eqMonth(v.getValue());
      } else if (v.getOper() == FilterOperation.GT_MONTH) {
        result = Filter.attr(attrName).gtMonth(v.getValue());
      } else if (v.getOper() == FilterOperation.GTE_MONTH) {
        result = Filter.attr(attrName).gteMonth(v.getValue());
      } else if (v.getOper() == FilterOperation.NEQ_MONTH) {
        result = Filter.attr(attrName).neqMonth(v.getValue());
      } else if (v.getOper() == FilterOperation.LT_MONTH) {
        result = Filter.attr(attrName).ltMonth(v.getValue());
      } else if (v.getOper() == FilterOperation.LTE_MONTH) {
        result = Filter.attr(attrName).lteMonth(v.getValue());
      }
    } else if (filter instanceof ValueFilter v) {
      String attrName = classInfo.wrapJpAttr(v.getAttrCode());
      if (v.getOper() == FilterOperation.EQ) {
        result = Filter.attr(attrName).eq(v.getValue());
      } else if (v.getOper() == FilterOperation.GT) {
        result = Filter.attr(attrName).gt(v.getValue());
      } else if (v.getOper() == FilterOperation.GTE) {
        result = Filter.attr(attrName).gte(v.getValue());
      } else if (v.getOper() == FilterOperation.NEQ) {
        result = Filter.attr(attrName).neq(v.getValue());
      } else if (v.getOper() == FilterOperation.LT) {
        result = Filter.attr(attrName).lt(v.getValue());
      } else if (v.getOper() == FilterOperation.LTE) {
        result = Filter.attr(attrName).lte(v.getValue());
      } else if (v.getOper() == FilterOperation.IN) {
        IN inst = (IN) v;
        result = Filter.attr(attrName).in(inst.getValue());
      } else if (v.getOper() == FilterOperation.NOT_IN) {
        NotIN inst = (NotIN) v;
        result = Filter.attr(attrName).notIn(inst.getValue());
      } else if (v.getOper() == FilterOperation.IN_QUERY) {
        INQuery inst = (INQuery) v;
        result = Filter.attr(attrName).inQuery(inst.getValue());
      } else if (v.getOper() == FilterOperation.NOT_IN_QUERY) {
        NotINQuery inst = (NotINQuery) v;
        result = Filter.attr(attrName).notInQuery(inst.getValue());
      } else if (v.getOper() == FilterOperation.ISNULL) {
        result = Filter.attr(attrName).isNull();
      } else if (v.getOper() == FilterOperation.ISNOTNULL) {
        result = Filter.attr(attrName).isNotNull();
      } else if (v.getOper() == FilterOperation.LIKE) {
        result = Filter.attr(attrName).like(v.getValue());
      } else if (v.getOper() == FilterOperation.FUZZY_LIKE) {
        FuzzyLike inst = (FuzzyLike) v;
        result = Filter.attr(attrName).fuzzyLike(inst.getValue());
      } else if (v.getOper() == FilterOperation.FUZZY_ORDER_LIKE) {
        FuzzyOrderLike inst = (FuzzyOrderLike) v;
        result = Filter.attr(attrName).fuzzyOrderLike(inst.getValue());
      } else if (v.getOper() == FilterOperation.STARTS_WITH) {
        result = Filter.attr(attrName).startWith(v.getValue());
      } else if (v.getOper() == FilterOperation.NOT_STARTS_WITH) {
        result = Filter.attr(attrName).notStartWith(v.getValue());
      } else if (v.getOper() == FilterOperation.CONTAINS_RANGE) {
        ContainsRange inst = (ContainsRange) v;
        result = Filter.attr(attrName).containsRange(inst.getValue());
      } else if (v.getOper() == FilterOperation.OVERLAPS_RANGE) {
        OverlapsRange inst = (OverlapsRange) v;
        result = Filter.attr(attrName).overlapsRange(inst.getValue());
      } else if (v.getOper() == FilterOperation.CONTAINS_ELEMENT) {
        result = Filter.attr(attrName).containsElement(v.getValue());
      } else if (v.getOper() == FilterOperation.EQ_RANGE) {
        EQRange inst = (EQRange) v;
        result = Filter.attr(attrName).eqRange(inst.getValue());
      } else if (v.getOper() == FilterOperation.GT_RANGE) {
        GTRange inst = (GTRange) v;
        result = Filter.attr(attrName).gtRange(inst.getValue());
      } else if (v.getOper() == FilterOperation.GTE_RANGE) {
        GTERange inst = (GTERange) v;
        result = Filter.attr(attrName).gteRange(inst.getValue());
      } else if (v.getOper() == FilterOperation.NEQ_RANGE) {
        NEQRange inst = (NEQRange) v;
        result = Filter.attr(attrName).neqRange(inst.getValue());
      } else if (v.getOper() == FilterOperation.LT_RANGE) {
        LTRange inst = (LTRange) v;
        result = Filter.attr(attrName).ltRange(inst.getValue());
      } else if (v.getOper() == FilterOperation.LTE_RANGE) {
        LTERange inst = (LTERange) v;
        result = Filter.attr(attrName).lteRange(inst.getValue());
      } else if (v.getOper() == FilterOperation.BETWEEN) {
        Between b = (Between) v;
        result = Filter.attr(attrName).between(b.getValue());
      } else if (v.getOper() == FilterOperation.CONTAINS) {
        ContainsKVP b = (ContainsKVP) v;
        result = Filter.attr(attrName).contains(b.getValue());
      } else if (v.getOper() == FilterOperation.SOFT_EQ) {
        SoftEQ s = (SoftEQ) v;
        result = Filter.attr(attrName).softEq(s.getValue());
      } else if (v.getOper() == FilterOperation.STRICT_EQ) {
        StrictEQ s = (StrictEQ) v;
        result = Filter.attr(attrName).strictEq(s.getValue());
      }
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