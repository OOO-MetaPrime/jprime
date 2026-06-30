package mp.jprime.utils.oktmo.oktmosearch;

import mp.jprime.common.annotations.JPParam;
import mp.jprime.meta.beans.JPType;
import mp.jprime.reactor.core.publisher.JPMono;
import mp.jprime.security.AuthInfo;
import mp.jprime.utils.JPUtil;
import mp.jprime.utils.annotations.JPUtilLink;
import mp.jprime.utils.annotations.JPUtilModeLink;
import mp.jprime.utils.oktmo.JpOktmoUtilsService;
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
public final class JpOktmoSearchUtil implements JPUtil {
  private final JpOktmoUtilsService oktmoService;

  private JpOktmoSearchUtil(@Autowired JpOktmoUtilsService oktmoService) {
    this.oktmoService = oktmoService;
  }

  @JPUtilModeLink(
      code = "search",
      title = "Поиск ОКТМО",
      inParams = {
          @JPParam(
              code = "query",
              type = JPType.STRING,
              description = "Поисковая строка"
          ),
          @JPParam(
              code = "limit",
              type = JPType.INT,
              description = "Ограничение по количеству"
          ),
          @JPParam(
              code = "subjectSearch",
              type = JPType.BOOLEAN,
              description = "Поиск по субъектам"
          ),
          @JPParam(
              code = "formationSearch",
              type = JPType.BOOLEAN,
              description = "Поиск по муниципальному уровню"
          ),
          @JPParam(
              code = "districtSearch",
              type = JPType.BOOLEAN,
              description = "Поиск по поселенческому уровню"
          ),
          @JPParam(
              code = "oktmoSearch",
              type = JPType.STRING,
              description = "Поиск с учетом указанных ОКТМО"
          ),
          @JPParam(
              code = "authSearch",
              type = JPType.BOOLEAN,
              description = "Поиск с учетом ОКТМО пользователя"
          )
      },
      actionLog = false,
      outClass = JsonSearchOut.class
  )
  public Mono<JsonSearchOut> search(JsonSearchIn in, AuthInfo auth) {
    return JPMono.fromCallable(() -> {
          Collection<JpOktmoUtilsService.Oktmo> list = oktmoService.search(
              in.getQuery(), in.getLimit(), JpOktmoUtilsService.SearchParams.of(
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
          Collection<JpOktmoUtilsService.Oktmo> list = oktmoService.get(in.getOktmo());
          return JsonGetOut.of(list);
        }
    );
  }

  @JPUtilModeLink(
      code = "groupSearch",
      title = "Поиск групп ОКТМО",
      inParams = {
          @JPParam(
              code = "query",
              type = JPType.STRING,
              description = "Поисковая строка"
          ),
          @JPParam(
              code = "limit",
              type = JPType.INT,
              description = "Ограничение по количеству"
          ),
          @JPParam(
              code = "prefixMode",
              type = JPType.BOOLEAN,
              description = "Возвращаем значимые префиксы ОКТМО, входящие в группу"
          ),
          @JPParam(
              code = "oktmoSearch",
              type = JPType.STRING,
              description = "Поиск с учетом указанных ОКТМО"
          ),
          @JPParam(
              code = "authSearch",
              type = JPType.BOOLEAN,
              description = "Поиск с учетом ОКТМО пользователя"
          )
      },
      actionLog = false,
      outClass = JsonGroupOut.class
  )
  public Mono<JsonGroupOut> groupSearch(JsonGroupSearchIn in, AuthInfo auth) {
    return JPMono.fromCallable(() -> {
          Collection<JpOktmoUtilsService.Group> list = oktmoService.groupSearch(
              in.getQuery(), in.getLimit(), in.isPrefixMode(),
              JpOktmoUtilsService.GroupSearchParams.of(
                  in.getOktmoSearch(), in.isAuthSearch(), auth
              ));
          return JsonGroupOut.of(list);
        }
    );
  }

  @JPUtilModeLink(
      code = "getGroup",
      title = "Названия групп ОКТМО",
      inParams = {
          @JPParam(
              code = "group",
              type = JPType.STRING_ARRAY,
              description = "Список групп ОКТМО"
          ),
          @JPParam(
              code = "prefixMode",
              type = JPType.BOOLEAN,
              description = "Возвращаем значимые префиксы ОКТМО, входящие в группу"
          ),
      },
      actionLog = false,
      outClass = JsonGroupOut.class
  )
  public Mono<JsonGroupOut> getGroup(JsonGroupGetIn in, AuthInfo auth) {
    return JPMono.fromCallable(() -> {
          Collection<JpOktmoUtilsService.Group> list = oktmoService.getGroup(in.getGroup(), in.isPrefixMode());
          return JsonGroupOut.of(list);
        }
    );
  }
}
