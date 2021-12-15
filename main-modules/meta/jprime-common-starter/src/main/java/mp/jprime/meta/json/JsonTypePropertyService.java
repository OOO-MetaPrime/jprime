package mp.jprime.meta.json;

import mp.jprime.meta.JPTypeService;
import mp.jprime.meta.beans.JPType;
import mp.jprime.meta.json.beans.JsonTypeProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервис по работе со свойствами атрибута
 */
@Service
public final class JsonTypePropertyService {
  /**
   * Логика по работе с типами данных
   */
  private JPTypeService jpTypeService;

  @Autowired
  private void setJpTypeService(JPTypeService jpTypeService) {
    this.jpTypeService = jpTypeService;
  }

  /**
   * Создаение JsonTypeProperty
   *
   * @param type JPType
   * @return JsonTypeProperty
   */
  public JsonTypeProperty from(JPType type) {
    return JsonTypeProperty.newBuilder(type.getCode())
        .availableChanges(jpTypeService.getAvailableChanges(type))
        .build();
  }
}
