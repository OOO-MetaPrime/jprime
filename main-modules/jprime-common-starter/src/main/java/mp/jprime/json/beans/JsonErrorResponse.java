package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collection;
import java.util.Date;

import static mp.jprime.formats.DateFormat.ISO8601;

/**
 * Данные REST ошибки
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonErrorResponse {
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ISO8601)
  private Date timestamp;
  private String path;
  private Integer status;
  private String error;
  private String message;
  private Collection<JsonErrorDetail> details;

  public JsonErrorResponse() {

  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Collection<JsonErrorDetail> getDetails() {
    return details;
  }

  public void setDetails(Collection<JsonErrorDetail> details) {
    this.details = details;
  }
}
