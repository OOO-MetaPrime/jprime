package mp.jprime.kafka.consumers;

/**
 * Динамический слушатель
 *
 * @param <K> тип ключа события
 * @param <V> тип значения события
 */
public interface JPKafkaDynamicConsumer<K, V> {
  /**
   * Признак автозапуска
   */
  boolean isAutoStart();

  /**
   * Запускает слушателя
   */
  void start();

  /**
   * Останавливает слушателя
   */
  void stop();

  /**
   * Ставит слушателя на паузу
   */
  void pause();

  /**
   * Снимает слушателя с паузы
   */
  void resume();

  /**
   * Топик, к которому подключен данный слушатель
   */
  String getTopic();
}
