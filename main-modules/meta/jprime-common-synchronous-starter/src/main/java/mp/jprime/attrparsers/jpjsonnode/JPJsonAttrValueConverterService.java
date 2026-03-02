package mp.jprime.attrparsers.jpjsonnode;

import mp.jprime.annotations.JPClassAttrsLink;
import mp.jprime.common.annotations.JPClassAttr;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.lang.JPJsonNode;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.beans.JPType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Сервис конвертации json-значений атрибута
 *
 * Инициируется набором {@link JPJsonAttrValueConverter} с настроенными привязками к атрибуту {@link JPClassAttrsLink}
 */
@Service
public final class JPJsonAttrValueConverterService {
  /**
   * Все конвертаторы
   */
  private final Map<String, Map<String, JPJsonAttrValueConverter>> converters = new HashMap<>();

  /**
   * Кеширование конвертаторов
   */
  @Autowired(required = false)
  private void setConverters(Collection<JPJsonAttrValueConverter> converters) {
    if (converters == null) {
      return;
    }
    for (JPJsonAttrValueConverter converter : converters) {
      JPClassAttrsLink converterAnno = converter.getClass().getAnnotation(JPClassAttrsLink.class);
      if (converterAnno == null) {
        continue;
      }
      JPClassAttr[] attrs = converterAnno.jpAttrs();
      if (attrs == null || attrs.length == 0) {
        continue;
      }
      for (JPClassAttr attr : attrs) {
        if (attr.jpClass() == null || attr.jpAttr() == null) {
          continue;
        }
        this.converters
            .computeIfAbsent(attr.jpClass(), x -> new HashMap<>())
            .put(attr.jpAttr(), converter);
      }
    }
  }

  /**
   * Конвертирует данные из формата хранения в формат представления
   *
   * @param jpo объект в отношении которого происходит трансформация
   * @param jpAttr Атрибут
   * @param value  Данные в формате хранения
   * @return Данные в формате представления
   */
  public JPJsonNode toJsonView(JPObject jpo, JPAttr jpAttr, JPJsonNode value) {
    if (jpAttr == null || jpAttr.getType() != JPType.JSON) {
      return null;
    }
    Map<String, JPJsonAttrValueConverter> maps = converters.get(jpAttr.getJpClassCode());
    JPJsonAttrValueConverter converter = maps == null ? null : maps.get(jpAttr.getCode());
    return converter == null ? value : converter.toJsonView(jpo, value);
  }

  /**
   * Конвертирует данные из формата представления в формат хранения
   *
   * @param jpo объект в отношении которого происходит трансформация
   * @param jpAttr Атрибут
   * @param value  Данные в формате представления
   * @return Данные в формате хранения
   */
  public JPJsonNode fromJsonView(JPObject jpo, JPAttr jpAttr, JPJsonNode value) {
    if (jpAttr == null || jpAttr.getType() != JPType.JSON) {
      return null;
    }
    Map<String, JPJsonAttrValueConverter> maps = converters.get(jpAttr.getJpClassCode());
    JPJsonAttrValueConverter converter = maps == null ? null : maps.get(jpAttr.getCode());
    return converter == null ? value : converter.fromJsonView(jpo, value);
  }
}
