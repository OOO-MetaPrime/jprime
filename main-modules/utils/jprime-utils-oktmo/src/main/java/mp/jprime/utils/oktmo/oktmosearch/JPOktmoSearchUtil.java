package mp.jprime.utils.oktmo.oktmosearch;

import mp.jprime.common.annotations.JPParam;
import mp.jprime.meta.beans.JPType;
import mp.jprime.reactor.core.publisher.JPMono;
import mp.jprime.security.AuthInfo;
import mp.jprime.utils.JPUtil;
import mp.jprime.utils.annotations.JPUtilLink;
import mp.jprime.utils.annotations.JPUtilModeLink;
import mp.jprime.utils.oktmo.JPOktmoUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.util.Collection;

import static mp.jprime.security.Role.AUTH_ACCESS;

/**
 * Утилита поиска ОКТМО
 */
@JPUtilLink(
    code = "oktmo-search",
    title = "Утилита поиска ОКТМО",
    authRoles = AUTH_ACCESS
)
public class JPOktmoSearchUtil implements JPUtil {
  private JPOktmoUtilsService oktmoService;

  @Autowired
  private void setOktmoService(JPOktmoUtilsService oktmoService) {
    this.oktmoService = oktmoService;
  }

  @JPUtilModeLink(
      code = "search",
      title = "Поиск ОКТМО",
      inParams = {
          @JPParam(
              code = "query",
              type = JPType.STRING,
              description = "поисковая строка"
          ),
          @JPParam(
              code = "limit",
              type = JPType.INT,
              description = "ограничение по количеству"
          ),
          @JPParam(
              code = "subjectSearch",
              type = JPType.BOOLEAN,
              description = "поиск по субъектам"
          ),
          @JPParam(
              code = "formationSearch",
              type = JPType.BOOLEAN,
              description = "поиск по образованиям"
          ),
          @JPParam(
              code = "districtSearch",
              type = JPType.BOOLEAN,
              description = "поиск по округам"
          ),
          @JPParam(
              code = "oktmoSearch",
              type = JPType.STRING,
              description = "поиск с учетом указанных ОКТМО"
          ),
          @JPParam(
              code = "authSearch",
              type = JPType.BOOLEAN,
              description = "поиск с учетом ОКТМО пользователя"
          )
      },
      actionLog = false,
      outClass = JsonSearchOut.class
  )
  public Mono<JsonSearchOut> search(JsonSearchIn in, AuthInfo auth) {
    return JPMono.fromCallable(() -> {
          Collection<JPOktmoUtilsService.Oktmo> list = oktmoService.search(
              in.getQuery(), in.getLimit(), JPOktmoUtilsService.SearchParams.of(
                  in.isSubjectSearch(), in.isFormationSearch(), in.isDistrictSearch(),
                  in.getOktmoSearch(), in.isAuthSearch(), auth
              ));
          return JsonSearchOut.of(list);
        }
    );
  }

  @JPUtilModeLink(
      code = "get",
      title = "Названия ОКТМО",
      inParams = {
          @JPParam(
              code = "oktmo",
              type = JPType.STRING_ARRAY,
              description = "Список ОКТМО"
          )
      },
      actionLog = false,
      outClass = JsonGetOut.class
  )
  public Mono<JsonGetOut> get(JsonGetIn in, AuthInfo auth) {
    return JPMono.fromCallable(() -> {
          Collection<JPOktmoUtilsService.Oktmo> list = oktmoService.get(in.getOktmo());
          return JsonGetOut.of(list);
        }
    );
  }
}
