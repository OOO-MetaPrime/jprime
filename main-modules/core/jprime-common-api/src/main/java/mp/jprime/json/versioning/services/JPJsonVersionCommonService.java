package mp.jprime.json.versioning.services;

import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.json.versioning.JPJsonBeanVersion;
import mp.jprime.json.versioning.JPJsonVersion;
import mp.jprime.json.versioning.JPJsonVersionConverter;
import mp.jprime.json.versioning.JPJsonVersionService;
import mp.jprime.json.versioning.beans.JPJsonBeanVersionBean;
import mp.jprime.json.versioning.beans.JPJsonVersionBean;
import mp.jprime.lang.JPJsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Сервис по трансформации данных в формате json с учетом версии
 */
@Service
public class JPJsonVersionCommonService implements JPJsonVersionService {
  private final Map<String, TreeMap<Integer, JPJsonVersionConverter<?>>> CACHE = new HashMap<>();

  private JPJsonMapper jsonMapper;

  @Autowired
  private void setJsonMapper(JPJsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  private JPJsonVersionCommonService(@Autowired(required = false) Collection<JPJsonVersionConverter<?>> converters) {
    if (converters == null || converters.isEmpty()) {
      return;
    }
    for (JPJsonVersionConverter<?> conv : converters) {
      if (conv.getVersion() == null || conv.getGroupCode() == null || conv.getBeanClass() == null) {
        continue;
      }
      CACHE.computeIfAbsent(conv.getGroupCode(), x -> new TreeMap<>())
          .put(conv.getVersion(), conv);
    }
  }

  @Override
  public <T> T toObject(String groupCode, JPJsonNode value) {
    Data data = getData(groupCode, value);
    if (data == null) {
      return null;
    }
    Integer version = data.bean.getVersion();
    JPJsonNode jsonData = data.bean.getData();

    JPJsonVersionConverter<?> conv = data.convs.get(version);
    return toBean(conv, jsonData);
  }

  @Override
  public <T> T toLatestObject(String groupCode, JPJsonNode value) {
    JPJsonBeanVersion<T> beanVersion = toLatestBeanVersion(groupCode, value);
    return beanVersion == null ? null : beanVersion.getData();
  }

  @Override
  public JPJsonVersion toVersion(String groupCode, JPJsonNode value) {
    Data data = getData(groupCode, value);
    if (data == null) {
      return null;
    }
    Integer version = data.bean.getVersion();
    JPJsonNode jsonData = data.bean.getData();

    return JPJsonVersionBean.of(version, jsonData);
  }

  @Override
  public JPJsonVersion toLatestVersion(String groupCode, JPJsonNode value) {
    Data data = getData(groupCode, value);
    if (data == null) {
      return null;
    }
    Integer version = data.bean.getVersion();
    JPJsonNode jsonData = data.bean.getData();

    JPJsonVersionConverter<?> lastVersion = data.convs.lastEntry().getValue();
    if (version.equals(lastVersion.getVersion())) {
      return JPJsonVersionBean.of(version, jsonData);
    }
    // иначе конвертируем последовательно
    return toVersion(lastVersion, version, jsonData, data.convs);
  }

  @Override
  public JPJsonVersion toLatestVersion(String groupCode, JPJsonVersion value) {
    TreeMap<Integer, JPJsonVersionConverter<?>> convs = CACHE.get(groupCode);
    if (convs == null) {
      return null;
    }
    Integer version = value.getVersion();
    JPJsonNode jsonData = value.getData();

    JPJsonVersionConverter<?> lastVersion = convs.lastEntry().getValue();
    if (version.equals(lastVersion.getVersion())) {
      return value;
    }
    // иначе конвертируем последовательно
    return toVersion(lastVersion, version, jsonData, convs);
  }

  /**
   * Трансформирует данные из json в бин версии
   *
   * @param groupCode Код группы версий
   * @param value     json данные
   * @return json
   */
  @Override
  public <T> JPJsonBeanVersion<T> toBeanVersion(String groupCode, JPJsonNode value) {
    Data data = getData(groupCode, value);
    if (data == null) {
      return null;
    }
    Integer version = data.bean.getVersion();
    JPJsonNode jsonData = data.bean.getData();

    JPJsonVersionConverter<?> conv = data.convs.get(version);
    return JPJsonBeanVersionBean.of(version, toBean(conv, jsonData));
  }

  /**
   * Трансформирует данные из json в бин версии
   *
   * @param groupCode Код группы версий
   * @param value     json данные
   * @return json
   */
  @Override
  public <T> JPJsonBeanVersion<T> toBeanVersion(String groupCode, JPJsonVersion value) {
    TreeMap<Integer, JPJsonVersionConverter<?>> convs = CACHE.get(groupCode);
    if (convs == null) {
      return null;
    }
    Integer version = value.getVersion();
    JPJsonNode jsonData = value.getData();

    JPJsonVersionConverter<?> conv = convs.get(version);
    return JPJsonBeanVersionBean.of(version, toBean(conv, jsonData));
  }

  /**
   * Трансформирует данные из json в бин последней версии
   *
   * @param groupCode Код группы версий
   * @param value     json данные
   * @return json
   */
  @Override
  public <T> JPJsonBeanVersion<T> toLatestBeanVersion(String groupCode, JPJsonNode value) {
    Data data = getData(groupCode, value);
    if (data == null) {
      return null;
    }
    Integer version = data.bean.getVersion();
    JPJsonNode jsonData = data.bean.getData();

    JPJsonVersionConverter<?> lastVersion = data.convs.lastEntry().getValue();
    if (version.equals(lastVersion.getVersion())) {
      return JPJsonBeanVersionBean.of(version, toBean(lastVersion, jsonData));
    }
    // иначе конвертируем последовательно
    JPJsonVersion jsonVersion = toVersion(lastVersion, version, jsonData, data.convs);
    if (jsonVersion == null) {
      return null;
    }
    return JPJsonBeanVersionBean.of(version, toBean(lastVersion, jsonVersion.getData()));
  }

  /**
   * Трансформирует данные из json в бин последней версии
   *
   * @param groupCode Код группы версий
   * @param value     json данные
   * @return json
   */
  @Override
  public <T> JPJsonBeanVersion<T> toLatestBeanVersion(String groupCode, JPJsonVersion value) {
    TreeMap<Integer, JPJsonVersionConverter<?>> convs = CACHE.get(groupCode);
    if (convs == null || value == null) {
      return null;
    }
    Integer version = value.getVersion();
    JPJsonNode jsonData = value.getData();

    JPJsonVersionConverter<?> lastVersion = convs.lastEntry().getValue();
    if (version.equals(lastVersion.getVersion())) {
      return JPJsonBeanVersionBean.of(version, toBean(lastVersion, jsonData));
    }
    // иначе конвертируем последовательно
    JPJsonVersion jsonVersion = toVersion(lastVersion, version, jsonData, convs);
    if (jsonVersion == null) {
      return null;
    }
    return JPJsonBeanVersionBean.of(version, toBean(lastVersion, jsonVersion.getData()));
  }

  private JPJsonVersion toVersion(JPJsonVersionConverter<?> cur, Integer version, JPJsonNode jsonData,
                                  TreeMap<Integer, JPJsonVersionConverter<?>> convs) {
    if (cur.fromVersions().contains(version)) {
      return JPJsonVersionBean.of(cur.getVersion(), cur.convertToNode(version, jsonData));
    } else {
      JPJsonVersionConverter<?> prev = convs.get(cur.getVersion() - 1);
      if (prev == null) {
        return null;
      }
      JPJsonVersion prevJsonVersion = toVersion(prev, version, jsonData, convs);
      if (prevJsonVersion == null) {
        return null;
      }
      return toVersion(cur, prevJsonVersion.getVersion(), prevJsonVersion.getData(), convs);
    }
  }

  private <T> T toBean(JPJsonVersionConverter<?> conv, JPJsonNode jsonData) {
    return conv != null ? (T) jsonMapper.toObject(conv.getBeanClass(), jsonData) : null;
  }

  private Data getData(String groupCode, JPJsonNode value) {
    if (value == null || groupCode == null) {
      return null;
    }
    TreeMap<Integer, JPJsonVersionConverter<?>> convs = CACHE.get(groupCode);
    if (convs == null) {
      return null;
    }
    JPJsonVersionBean bean = jsonMapper.toObject(JPJsonVersionBean.class, value);
    Integer version = bean.getVersion();
    if (version == null) {
      return null;
    }
    return new Data(bean, convs);
  }

  /**
   * Данные для обработки
   */
  private static class Data {
    private final JPJsonVersion bean;
    private final TreeMap<Integer, JPJsonVersionConverter<?>> convs;

    private Data(JPJsonVersion bean, TreeMap<Integer, JPJsonVersionConverter<?>> convs) {
      this.bean = bean;
      this.convs = convs;
    }
  }
}
