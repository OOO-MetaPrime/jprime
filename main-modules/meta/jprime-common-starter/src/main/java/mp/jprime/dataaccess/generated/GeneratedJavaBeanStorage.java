package mp.jprime.dataaccess.generated;

import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.checkers.JPDataCheckService;
import mp.jprime.dataaccess.checkers.JPDataCheckServiceAware;
import mp.jprime.dataaccess.generated.storages.GeneratedJPClassBaseStorage;
import mp.jprime.dataaccess.params.JPSelect;
import mp.jprime.meta.services.JPBeanService;
import mp.jprime.meta.services.JPMetaStorage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Map;

/**
 * Класс для репозитория только на чтение для данных, генерируемых на основе java бинов
 */
public abstract class GeneratedJavaBeanStorage extends GeneratedJPClassBaseStorage implements JPDataCheckServiceAware {
  // Хранилище метаинформации
  private JPMetaStorage metaStorage;
  // Логика работы с JPBean
  private JPBeanService beanService;
  // Сервис проверки данных указанному условию
  private JPDataCheckService dataCheckService;

  @Override
  public void setJpDataCheckService(JPDataCheckService dataCheckService) {
    this.dataCheckService = dataCheckService;
  }

  @Autowired
  private void setMetaStorage(JPMetaStorage metaStorage) {
    this.metaStorage = metaStorage;
  }

  @Autowired
  private void setBeanService(JPBeanService beanService) {
    this.beanService = beanService;
  }

  /**
   * Возвращает весь список объектов
   *
   * @return Список объектов
   */
  protected abstract Collection<JPObject> getValues();

  /**
   * Создает JPObject на основе данных
   *
   * @param jpClassCode Кодовое имя класса
   * @param data        Данные
   * @return JPObject
   */
  protected JPObject toJPObject(String jpClassCode, Map<String, Object> data) {
    return beanService.newInstance(
        metaStorage.getJPClassByCode(jpClassCode),
        data
    );
  }

  @Override
  public Collection<JPObject> getList(JPSelect select) {
    return dataCheckService.getList(select, getValues());
  }

  @Override
  public Long getTotalCount(JPSelect select) {
    return dataCheckService.getTotalCount(select, getValues());
  }
}
