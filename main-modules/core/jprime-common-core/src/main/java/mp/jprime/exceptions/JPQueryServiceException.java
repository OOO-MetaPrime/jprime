package mp.jprime.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ошибка обращения к несуществующим сервисам
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class JPQueryServiceException extends JPRuntimeException {
  public JPQueryServiceException() {
    super("query.service.error", "Не найден сервис для обработки запроса");
  }

  private JPQueryServiceException(String messageCode, String serviceCode, String code) {
    super(
        "query.service." + messageCode,
        "Не найден " + serviceCode + " для настройки с кодом \"" + code + "\""
    );
  }

  public static JPQueryServiceException fromJdbcTemplate(String code) {
    return new JPQueryServiceException("jdbcTemplate.notFound", "JdbcTemplate", code);
  }

  public static JPQueryServiceException fromNamedJdbcTemplate(String code) {
    return new JPQueryServiceException("namedJdbcTemplate.notFound", "NamedJdbcTemplate", code);
  }

  public static JPQueryServiceException fromSQLTemplates(String code) {
    return new JPQueryServiceException("sqlTemplates.notFound", "SQLTemplates", code);
  }

  public static JPQueryServiceException fromDataSource(String code) {
    return new JPQueryServiceException("dataSource.notFound", "DataSource", code);
  }

  public static JPQueryServiceException fromTransactionManager(String code) {
    return new JPQueryServiceException("transactionManager.notFound", "TransactionManager", code);
  }
}
