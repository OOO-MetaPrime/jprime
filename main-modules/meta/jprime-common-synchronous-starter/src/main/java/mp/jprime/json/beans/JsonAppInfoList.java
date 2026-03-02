package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import mp.jprime.system.JPAppProperty;

import java.util.Collection;
import java.util.Collections;

/**
 * Список описания приложений.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
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
   * Формируем json на основании данных
   *
   * @param apps Список JsonAppInfo
   * @return JsonAppInfoList
   */
  public static JsonAppInfoList of(Collection<JsonAppInfo> apps) {
    return new JsonAppInfoList(apps);
  }

  /**
   * Формируем json на основании JPAppProperty
   *
   * @param appProperty JPAppProperty
   * @return JsonAppInfoList
   */
  public static JsonAppInfoList from(JPAppProperty appProperty) {
    return JsonAppInfoList.of(Collections.singleton(
        JsonAppInfo.newBuilder()
            .code(appProperty.applicationCode())
            .title(appProperty.applicationTitle())
            .build()
    ));
  }

  /**
   * Список описаний приложений
   *
   * @return Список приложений
   */
  public Collection<JsonAppInfo> getApps() {
    return apps;
  }
}
