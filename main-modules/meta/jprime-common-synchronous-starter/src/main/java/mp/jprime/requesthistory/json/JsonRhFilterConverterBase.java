package mp.jprime.requesthistory.json;

import mp.jprime.requesthistory.RequestHistoryFilter;
import mp.jprime.requesthistory.beans.RequestHistoryFilterBean;
import mp.jprime.requesthistory.json.beans.JsonRequestHistoryFilter;
import org.springframework.stereotype.Service;

/**
 * Конвертер фильтров истории запросов
 */
@Service
public class JsonRhFilterConverterBase {
  /**
   * Конвертировать {@link JsonRequestHistoryFilter} -> {@link RequestHistoryFilter}
   *
   * @param filter json-настройки фильтра
   * @return Настройки фильтра
   */
  public RequestHistoryFilter toFilter(JsonRequestHistoryFilter filter) {
    if (filter == null) {
      return null;
    }

    return RequestHistoryFilterBean.newBuilder()
        .classCode(filter.getClassCode())
        .userId(filter.getUserId())
        .username(filter.getUsername())
        .requestDateFrom(filter.getRequestDateFrom())
        .requestDateTo(filter.getRequestDateTo())
        .objectId(filter.getObjectId())
        .build();
  }

  /**
   * Конвертировать {@link RequestHistoryFilter} -> {@link JsonRequestHistoryFilter}
   *
   * @param filter Настройки фильтра
   * @return json-настройки фильтра
   */
  public JsonRequestHistoryFilter toJsonFilter(RequestHistoryFilter filter) {
    if (filter == null) {
      return null;
    }

    return JsonRequestHistoryFilter.newBuilder()
        .classCode(filter.getClassCode())
        .userId(filter.getUserId())
        .username(filter.getUsername())
        .requestDateFrom(filter.getRequestDateFrom())
        .requestDateTo(filter.getRequestDateTo())
        .objectId(filter.getObjectId())
        .build();
  }
}
