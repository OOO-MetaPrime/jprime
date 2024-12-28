package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Список описания приложений.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonAppInfoList {
  private Collection<JsonAppInfo> apps;

  /**
   * Конструктор
   */
  public JsonAppInfoList() {

  }

  /**
   * Конструктор
   *
   * @param apps Список описаний приложений
   */
  private JsonAppInfoList(Collection<JsonAppInfo> apps) {
    this.apps = apps != null ? apps : Collections.emptyList();
  }

  /**
   * Список описаний приложений
   *
   * @return Список приложений
   */
  public Collection<JsonAppInfo> getApps() {
    return apps;
  }

  /**
   * Построитель JsonAppInfoList
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель JsonAppInfoList
   */
  public static final class Builder {
    private Collection<JsonAppInfo> apps = new ArrayList<>();

    private Builder() {
    }

    /**
     * Создаем JsonAppInfoList
     *
     * @return JsonAppInfoList
     */
    public JsonAppInfoList build() {
      return new JsonAppInfoList(apps);
    }

    /**
     * Список описаний приложений
     *
     * @param apps Список приложений
     * @return Builder
     */
    public Builder apps(Collection<JsonAppInfo> apps) {
      this.apps = apps;
      return this;
    }
  }
}
