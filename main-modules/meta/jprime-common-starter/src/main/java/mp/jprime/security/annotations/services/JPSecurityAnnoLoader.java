package mp.jprime.security.annotations.services;

import mp.jprime.security.JPSecurityLoader;
import mp.jprime.security.JPSecurityPackage;
import mp.jprime.security.JPSecuritySettings;
import mp.jprime.security.annotations.JPAccess;
import mp.jprime.security.annotations.JPPackage;
import mp.jprime.security.annotations.JPPackages;
import mp.jprime.security.beans.JPSecurityPackageAccessBean;
import mp.jprime.security.beans.JPAccessType;
import mp.jprime.security.beans.JPSecurityPackageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.Collection;

/**
 * Загрузка метаинформации по аннотациям
 */
@Service
public class JPSecurityAnnoLoader implements JPSecurityLoader {
  private Collection<JPSecuritySettings> setts;

  /**
   * Считываем аннотации
   */
  @Autowired(required = false)
  private void setSetts(Collection<JPSecuritySettings> setts) {
    this.setts = setts;
  }

  /**
   * Вычитывает метаописание
   *
   * @return Список метаописания
   */
  @Override
  public Flux<JPSecurityPackage> load() {
    return Flux.create(x -> {
      loadTo(x);
      x.complete();
    });
  }

  private void loadTo(FluxSink<JPSecurityPackage> sink) {
    if (setts == null || setts.isEmpty()) {
      return;
    }
    for (JPSecuritySettings s : setts) {
      JPPackages p = s.getClass().getAnnotation(JPPackages.class);
      if (p == null) {
        continue;
      }
      JPPackage[] packages = p.value();
      if (packages.length == 0) {
        continue;
      }
      for (JPPackage pack : packages) {
        JPAccess[] accesses = pack.access();
        if (accesses.length == 0) {
          continue;
        }
        JPSecurityPackageBean.Builder builder = JPSecurityPackageBean.newBuilder()
            .code(pack.code())
            .name(pack.name())
            .description(pack.description())
            .qName(pack.qName());

        for (JPAccess access : accesses) {
          JPSecurityPackageAccessBean accessBean = JPSecurityPackageAccessBean.newBuilder()
              .role(access.role())
              .create(access.create())
              .read(access.read())
              .delete(access.delete())
              .update(access.update())
              .build();
          if (access.type() == JPAccessType.PERMIT) {
            builder.permitAccess(accessBean);
          } else {
            builder.prohibitionAccess(accessBean);
          }
        }
        sink.next(builder.build());
      }
    }
  }
}
