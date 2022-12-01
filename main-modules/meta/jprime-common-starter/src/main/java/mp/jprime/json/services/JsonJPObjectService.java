package mp.jprime.json.services;

import mp.jprime.attrparsers.base.JPJsonParser;
import mp.jprime.attrparsers.jpjsonnode.JPJsonAttrValueConverterService;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.beans.JPObjectAccess;
import mp.jprime.json.beans.JsonChangeAccess;
import mp.jprime.json.beans.JsonJPObject;
import mp.jprime.lang.JPJsonNode;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.beans.JPType;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.rest.v1.Controllers;
import mp.jprime.web.services.ServerWebExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import java.util.Collection;
import java.util.Map;

/**
 * Формирование JsonJPObject и JsonObjectData
 */
@Service
public class JsonJPObjectService {
  /**
   * Хранилище метаинформации
   */
  private JPMetaStorage metaStorage;
  /**
   * Сервис конвертации json-значений атрибута
   */
  private JPJsonAttrValueConverterService jsonAttrValueConverterService;
  /**
   * Реализация парсера {@link JPType#JSON}
   */
  private JPJsonParser jpJsonParser;
  /**
   * Методы работы с ServerWebExchangeService
   */
  private ServerWebExchangeService sweService;
  /**
   * Признак добавления блока links
   */
  @Value("${jprime.api.addLinks:false}")
  private boolean addLinks;

  @Autowired
  private void setMetaStorage(JPMetaStorage metaStorage) {
    this.metaStorage = metaStorage;
  }

  @Autowired
  private void setJsonAttrValueConverterService(JPJsonAttrValueConverterService jsonAttrValueConverterService) {
    this.jsonAttrValueConverterService = jsonAttrValueConverterService;
  }

  @Autowired
  private void setJpJsonParser(JPJsonParser jpJsonParser) {
    this.jpJsonParser = jpJsonParser;
  }

  @Autowired
  private void setSweService(ServerWebExchangeService sweService) {
    this.sweService = sweService;
  }

  public JsonJPObject toJsonJPObject(JPObject object, ServerWebExchange swe) {
    return toJsonJPObject(object, null, swe);
  }

  public JsonJPObject toJsonJPObject(JPObject object, JPObjectAccess objectAccess, ServerWebExchange swe) {
    if (object == null) {
      return JsonJPObject.newBuilder().build();
    }

    return JsonJPObject.newBuilder()
        .metaStorage(metaStorage)
        .jpId(object.getJpId())
        .jpData(getJPData(object.getJpClassCode(), object.getData()))
        .jpLinkedData(object.getLinkedData())
        .baseUrl(sweService.getBaseUrl(swe))
        .restMapping(Controllers.API_MAPPING)
        .addLinks(addLinks)
        .access(objectAccess == null ? null : JsonChangeAccess.newBuilder()
            .update(
                objectAccess.isUpdate()
            )
            .delete(
                objectAccess.isDelete()
            )
            .build()
        )
        .build();
  }

  private JPData getJPData(String objectJPClass, JPData objectData) {
    JPClass cls = metaStorage.getJPClassByCode(objectJPClass);
    if (cls == null) {
      return objectData;
    }
    // При наличии json атрибутов конвертируем их представление к пользовательскому
    Collection<JPAttr> jsonAttrs = cls.getAttrs(JPType.JSON);
    if (jsonAttrs.isEmpty()) {
      return objectData;
    }
    Map<String, Object> data = objectData.toMap();
    jsonAttrs.forEach(jpAttr -> {
      // старое значение
      Object value = objectData.get(jpAttr);
      // сконвертированное новое
      data.put(
          jpAttr.getCode(),
          jsonAttrValueConverterService.toJsonView(jpAttr, jpJsonParser.parse(jpAttr, value))
      );
    });
    return JPData.of(data);
  }
}
