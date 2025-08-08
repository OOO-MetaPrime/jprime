package mp.jprime.security.json.converters;

import mp.jprime.dataaccess.JPObjectAccessService;
import mp.jprime.dataaccess.JPObjectAccessServiceAware;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.json.beans.JsonJPClassAccess;
import mp.jprime.security.json.beans.JsonJPObjectAccess;
import mp.jprime.security.services.JPSecurityStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JsonAccessConverter implements JPObjectAccessServiceAware {
  private JPSecurityStorage securityManager;
  private JPObjectAccessService objectAccessService;

  @Autowired
  private void setSecurityManager(JPSecurityStorage securityManager) {
    this.securityManager = securityManager;
  }

  @Override
  public void setJpObjectAccessService(JPObjectAccessService objectAccessService) {
    this.objectAccessService = objectAccessService;
  }

  public JsonJPClassAccess toJPClassAccess(JPClass jpClass, AuthInfo auth) {
    return toJPClassAccess(jpClass, null, null, auth);
  }

  public JsonJPClassAccess toJPClassAccess(JPClass jpClass, String refAttrCode, Comparable value, AuthInfo auth) {
    JsonJPClassAccess.Builder builder = JsonJPClassAccess.newBuilder()
        .classCode(jpClass.getCode())
        .read(
            objectAccessService.checkRead(jpClass.getCode(), auth)
        )
        .create(
            objectAccessService.checkCreate(jpClass.getCode(), refAttrCode, value, auth)
        )
        .update(
            objectAccessService.checkUpdate(jpClass.getCode(), auth)
        )
        .delete(
            objectAccessService.checkDelete(jpClass.getCode(), auth)
        );
    // Доступ к атрибутам
    jpClass.getAttrs().stream()
        .filter(attr -> securityManager.checkRead(attr.getJpPackage(), auth.getRoles()))
        .forEach(attr -> builder.attrEdit(
            attr.getCode(), securityManager.checkCreate(attr.getJpPackage(), auth.getRoles())
        ));
    return builder.build();
  }

  public Collection<JsonJPObjectAccess> toJPObjectAccess(JPClass jpClass, Collection<? extends Comparable> objectIds, AuthInfo auth) {
    //Доступ к атрибутам
    Map<String, Boolean> attrAccess = jpClass.getAttrs().stream()
        .filter(attr -> securityManager.checkRead(attr.getJpPackage(), auth.getRoles()))
        .collect(Collectors.toMap(JPAttr::getCode, attr -> attr.isUpdatable() && securityManager.checkUpdate(attr.getJpPackage(), auth.getRoles())));

    return objectAccessService.objectsAccess(
            jpClass,
            objectIds,
            auth)
        .stream()
        .map(a -> JsonJPObjectAccess.newBuilder()
            .objectId(a.getId().toString())
            .objectClassCode(a.getJpClass())
            .read(a.isRead())
            .update(a.isUpdate())
            .delete(a.isDelete())
            .create(a.isCreate())
            .attrEdit(attrAccess)
            .build())
        .collect(Collectors.toList());
  }
}
