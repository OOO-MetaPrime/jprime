package mp.jprime.api.rest.controllers;

import mp.jprime.dataaccess.JPObjectRepositoryService;
import mp.jprime.json.services.QueryService;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.rest.v1.RestApiBaseController;
import mp.jprime.security.jwt.JWTService;
import mp.jprime.web.services.ServerWebExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class RestApiController implements RestApiBaseController {
  /**
   * Возвращает маппинг контроллера
   *
   * @return Маппинг контроллера
   */
  public String getApiMapping() {
    return "api/v1";
  }

  /**
   * Заполнение запросов на основе JSON
   */
  private QueryService queryService;
  /**
   * Интерфейс создания / обновления объекта
   */
  private JPObjectRepositoryService repo;
  /**
   * Хранилище метаинформации
   */
  private JPMetaStorage metaStorage;
  /**
   * Методы работы с ServerWebExchangeService
   */
  private ServerWebExchangeService sweService;
  /**
   * Обработчик JWT
   */
  private JWTService jwtService;


  @Value("${jprime.query.queryTimeout:}")
  private Integer queryTimeout;

  @Autowired
  private void setQueryService(QueryService queryService) {
    this.queryService = queryService;
  }

  @Autowired
  private void setRepo(JPObjectRepositoryService repo) {
    this.repo = repo;
  }

  @Autowired
  private void setMetaStorage(JPMetaStorage metaStorage) {
    this.metaStorage = metaStorage;
  }

  @Autowired
  private void setSweService(ServerWebExchangeService sweService) {
    this.sweService = sweService;
  }

  @Autowired
  private void setJwtService(JWTService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  public QueryService getQueryService() {
    return queryService;
  }

  @Override
  public JPObjectRepositoryService getRepo() {
    return repo;
  }

  @Override
  public JPMetaStorage getMetaStorage() {
    return metaStorage;
  }

  @Override
  public ServerWebExchangeService getSweService() {
    return sweService;
  }

  @Override
  public JWTService getJwtService() {
    return jwtService;
  }

  @Override
  public Integer getQueryTimeout() {
    return queryTimeout;
  }
}
