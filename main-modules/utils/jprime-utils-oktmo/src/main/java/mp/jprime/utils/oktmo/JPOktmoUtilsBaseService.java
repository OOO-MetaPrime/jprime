package mp.jprime.utils.oktmo;

import jakarta.annotation.PostConstruct;
import mp.jprime.dataaccess.JPObjectRepositoryService;
import mp.jprime.dataaccess.JPObjectRepositoryServiceAware;
import mp.jprime.dataaccess.params.JPSelect;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.json.services.QueryService;
import mp.jprime.parsers.ParserService;
import mp.jprime.parsers.ParserServiceAware;
import mp.jprime.security.AuthInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Базовая реализация сервиса поиска по ОКТМО
 */
public abstract class JPOktmoUtilsBaseService implements JPOktmoUtilsService, JPObjectRepositoryServiceAware, ParserServiceAware {
  private JPObjectRepositoryService repo;
  private ParserService parserService;
  private static final Pattern PATTERN = Pattern.compile("\\d+");

  @PostConstruct
  private void init() {
    if (StringUtils.isAnyBlank(getClassCode(), getOktmoAttr(), getNameAttr(), getTypeAttr())) {
      throw new JPRuntimeException(String.format("OKTMO class code or attributes cannot be null or empty. classCode: %s, oktmo: %s, oktmoName: %s, oktmoType: %s",
          getClassCode(), getOktmoAttr(), getNameAttr(), getTypeAttr()));
    }
  }

  @Override
  public void setJpObjectRepositoryService(JPObjectRepositoryService repositoryService) {
    this.repo = repositoryService;
  }

  @Override
  public void setParserService(ParserService parserService) {
    this.parserService = parserService;
  }

  /**
   * Возвращает описание по переданным кодам ОКТМО
   *
   * @param oktmoList Коды ОКТМО
   * @return Список ОКТМО
   */
  @Override
  public Collection<Oktmo> get(Collection<String> oktmoList) {
    if (oktmoList == null || oktmoList.isEmpty()) {
      return Collections.emptyList();
    }

    String oktmo = getOktmoAttr();
    String oktmoName = getNameAttr();

    return repo.getList(
            JPSelect.from(getClassCode())
                .attr(oktmo)
                .attr(oktmoName)
                .where(
                    Filter.attr(oktmo).in(oktmoList)
                )
                .build()
        ).stream()
        .map(x -> Oktmo.of(
            parserService.toString(x.getAttrValue(oktmo)),
            parserService.toString(x.getAttrValue(oktmoName))
        ))
        .toList();
  }

  /**
   * Поиск ОКТМО по параметрам
   *
   * @param query  Поисковая строка
   * @param limit  Количество объектов в выборке
   * @param params Настройки поиска ОКТМО
   * @return Список ОКТМО
   */
  @Override
  public Collection<Oktmo> search(String query, Integer limit, SearchParams params) {
    if (StringUtils.isBlank(query) || params == null) {
      return Collections.emptyList();
    }
    AuthInfo auth = params.getAuth();
    if (params.isAuthSearch() && auth == null) {
      return Collections.emptyList();
    }

    // По каким ОКТМО производится поиск
    Collection<Integer> searchOktmoType = new ArrayList<>(3);
    if (params.isSubjectSearch()) {
      searchOktmoType.add(getSubjectCode());
    }
    if (params.isFormationSearch()) {
      searchOktmoType.add(getFormationCode());
    }
    if (params.isDistrictSearch()) {
      searchOktmoType.add(getDistrictCode());
    }

    if (searchOktmoType.isEmpty()) {
      return Collections.emptyList();
    }

    String oktmo = getOktmoAttr();
    String oktmoName = getNameAttr();

    // Фильтр по явно указанным ОКТМО
    Collection<String> oktmoPrefix = mp.jprime.utils.Oktmo.getPrefix(params.getOktmoSearch());
    Collection<Filter> prefixFilters = oktmoPrefix == null || oktmoPrefix.isEmpty() ? null : oktmoPrefix.stream()
        .map(x -> Filter.attr(oktmo).startWith(x))
        .collect(Collectors.toList());

    // Фильтр ОКТМО по пользователю
    Collection<String> authOktmoPrefix = params.isAuthSearch() && auth != null ? auth.getOktmoPrefixList() : null;
    Collection<Filter> authPrefixFilters = authOktmoPrefix == null || authOktmoPrefix.isEmpty() ? null : authOktmoPrefix.stream()
        .map(x -> Filter.attr(oktmo).startWith(x))
        .collect(Collectors.toList());

    Matcher matcher = PATTERN.matcher(query);
    boolean isFind = matcher.find();
    String oktmoQuery = isFind ? matcher.group() : null;
    String nameQuery = isFind ? matcher.replaceAll("").trim() : query;

    return repo.getList(
            JPSelect.from(getClassCode())
                .attr(oktmo)
                .attr(oktmoName)
                .where(
                    Filter.and(
                        Filter.attr(getTypeAttr()).in(searchOktmoType),
                        prefixFilters == null ? null : Filter.or(prefixFilters),
                        authPrefixFilters == null ? null : Filter.or(authPrefixFilters),
                        oktmoQuery == null ? null : Filter.attr(oktmo).startWith(oktmoQuery),
                        nameQuery.isEmpty() ? null : Filter.attr(oktmoName).like(nameQuery)
                    )
                )
                .limit(limit != null ? limit : QueryService.MAX_LIMIT)
                .build()
        ).stream()
        .map(x -> Oktmo.of(
            parserService.toString(x.getAttrValue(oktmo)),
            parserService.toString(x.getAttrValue(oktmoName))
        ))
        .toList();
  }

  /**
   * Возвращает код класса ОКТМО
   *
   * @return Код класса ОКТМО
   */
  protected abstract String getClassCode();

  /**
   * Возвращает атрибут 'ОКТМО'
   *
   * @return атрибут 'ОКТМО'
   */
  protected abstract String getOktmoAttr();

  /**
   * Возвращает атрибут 'Название ОКТМО'
   *
   * @return Атрибут 'Название ОКТМО'
   */
  protected abstract String getNameAttr();

  /**
   * Возвращает атрибут 'Тип ОКТМО'
   *
   * @return Атрибут 'Тип ОКТМО'
   */
  protected abstract String getTypeAttr();

  /**
   * Возвращает код типа ОКТМО
   *
   * @return Код субъекта РФ
   */
  protected abstract int getSubjectCode();

  /**
   * Возвращает код типа ОКТМО
   *
   * @return Код муниципального образования
   */
  protected abstract int getFormationCode();

  /**
   * Возвращает код типа ОКТМО
   *
   * @return Код муниципального округа
   */
  protected abstract int getDistrictCode();
}
