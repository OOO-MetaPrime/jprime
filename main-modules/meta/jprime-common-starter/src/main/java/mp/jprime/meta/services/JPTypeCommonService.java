package mp.jprime.meta.services;

import mp.jprime.meta.beans.JPType;
import mp.jprime.meta.JPTypeService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * Логика по работе с типами данных
 */
@Service
public final class JPTypeCommonService implements JPTypeService {
  /**
   * Матрица смены типов атрибута
   */
  private static final Map<JPType, Collection<JPType>> CHANGE_AVAILABLE_TYPES = new EnumMap<>(JPType.class);

  static {
    BiConsumer<JPType, Collection<JPType>> func = (k, v) -> CHANGE_AVAILABLE_TYPES.put(k, Collections.unmodifiableCollection(v));

    func.accept(JPType.BACKREFERENCE, Collections.emptyList());
    func.accept(JPType.BIGINT, Arrays.asList(JPType.STRING, JPType.MONEY, JPType.FILE));
    func.accept(JPType.BOOLEAN, Arrays.asList(JPType.STRING, JPType.INT, JPType.SIMPLEFRACTION, JPType.MONEY, JPType.FILE));
    func.accept(JPType.DATE, Arrays.asList(JPType.STRING, JPType.TIMESTAMP, JPType.DATETIME, JPType.FILE));
    func.accept(JPType.DATETIME, Arrays.asList(JPType.STRING, JPType.TIMESTAMP, JPType.FILE));
    func.accept(JPType.DOUBLE, Arrays.asList(JPType.STRING, JPType.MONEY, JPType.FILE));
    func.accept(JPType.FILE, Collections.singletonList(JPType.STRING));
    func.accept(JPType.FLOAT, Arrays.asList(JPType.STRING, JPType.DOUBLE, JPType.MONEY, JPType.FILE));
    func.accept(JPType.INT, Arrays.asList(JPType.STRING, JPType.BIGINT, JPType.LONG, JPType.FLOAT, JPType.DOUBLE, JPType.SIMPLEFRACTION, JPType.MONEY, JPType.FILE));
    func.accept(JPType.JSON, Arrays.asList(JPType.STRING, JPType.FILE));
    func.accept(JPType.LONG, Arrays.asList(JPType.STRING, JPType.BIGINT, JPType.DOUBLE, JPType.MONEY, JPType.FILE));
    func.accept(JPType.MONEY, Arrays.asList(JPType.STRING, JPType.FLOAT, JPType.DOUBLE, JPType.FILE));
    func.accept(JPType.SIMPLEFRACTION, Arrays.asList(JPType.STRING, JPType.BIGINT, JPType.INT, JPType.LONG, JPType.FLOAT, JPType.DOUBLE, JPType.MONEY, JPType.FILE));
    func.accept(JPType.STRING, Collections.singletonList(JPType.FILE));
    func.accept(JPType.TIME, Arrays.asList(JPType.STRING, JPType.TIMESTAMP, JPType.DATETIME, JPType.FILE));
    func.accept(JPType.TIMESTAMP, Arrays.asList(JPType.STRING, JPType.DATETIME, JPType.FILE));
    func.accept(JPType.UUID, Arrays.asList(JPType.STRING, JPType.FILE));
    func.accept(JPType.VIRTUALREFERENCE, Collections.emptyList());
    func.accept(JPType.XML, Arrays.asList(JPType.STRING, JPType.FILE));
  }

  /**
   * Возвращает список типов, на которые можно менять
   *
   * @param type Тип атрибута
   * @return Список типов, на которые можно изменить указанный
   */
  @Override
  public Collection<JPType> getAvailableChanges(JPType type) {
    Collection<JPType> types = type == null ? null : CHANGE_AVAILABLE_TYPES.get(type);
    return types != null ? types : Collections.emptyList();
  }
}
