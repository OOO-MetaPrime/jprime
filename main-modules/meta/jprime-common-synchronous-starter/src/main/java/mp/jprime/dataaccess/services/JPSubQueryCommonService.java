package mp.jprime.dataaccess.services;

import mp.jprime.dataaccess.JPSubQueryService;
import mp.jprime.dataaccess.JPSyncObjectRepositoryService;
import mp.jprime.dataaccess.JPSyncObjectRepositoryServiceAware;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.params.JPSelect;
import mp.jprime.dataaccess.params.JPSubQuery;
import mp.jprime.lang.JPArray;
import mp.jprime.security.AuthInfo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

@Service
public final class JPSubQueryCommonService implements JPSubQueryService, JPSyncObjectRepositoryServiceAware {
  // Ограничиваем 100 объектами
  private final static int LIMIT = 100;

  private JPSyncObjectRepositoryService repo;

  @Override
  public void setJpSyncObjectRepositoryService(JPSyncObjectRepositoryService repo) {
    this.repo = repo;
  }

  @Override
  public Collection<Comparable> getValues(JPSubQuery subQuery, AuthInfo auth) {
    String attr = subQuery.getAttr();

    Collection<JPObject> list = repo.getList(
        JPSelect.from(subQuery.getClassCode())
            .attr(attr)
            .where(subQuery.getFilter())
            .auth(auth)
            .limit(LIMIT)
            .build()
    );
    if (list == null || list.isEmpty()) {
      return Collections.emptyList();
    }
    Collection<Comparable> result = new HashSet<>();
    for (JPObject object : list) {
      Object value = object.getAttrValue(attr);
      if (value == null) {
        continue;
      }
      if (value instanceof JPArray<?> a) {
        result.addAll(a.toList());
      } else if (value instanceof Comparable c) {
        result.add(c);
      }
    }
    return result;
  }
}
