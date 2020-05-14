package mp.jprime.meta;

/**
 * Метаописание классов
 */
public abstract class JPMeta {
  public interface Attr {
    /**
     * Глобальный идентификатор объекта
     */
    String UUID = "uuid";
    /**
     * Пользователь, создавший объект
     */
    String USEROWNERID = "userOwnerId";
    /**
     * Дата создания
     */
    String CREATIONDATE = "creationDate";
    /**
     * Пользователь, изменивший объект
     */
    String USEREDITORID = "userEditorId";
    /**
     * Дата редактирования
     */
    String CHANGEDATE = "changeDate";
    /**
     * Настройка доступа к объекту
     */
    String JPPACKAGE = "jpPackage";
  }
}
