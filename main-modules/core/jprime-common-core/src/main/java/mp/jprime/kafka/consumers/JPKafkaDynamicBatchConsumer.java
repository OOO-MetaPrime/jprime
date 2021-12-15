package mp.jprime.kafka.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.listener.BatchMessageListener;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.util.Assert;

/**
 * Динамический пакетный слушатель
 *
 * @param <K> тип ключа события
 * @param <V> тип значения события
 */
public class JPKafkaDynamicBatchConsumer<K, V> implements JPKafkaDynamicConsumer<K, V> {
  private static final Logger LOG = LoggerFactory.getLogger(JPKafkaDynamicBatchConsumer.class);

  private final String topic;
  private final MessageListenerContainer messageListenerContainer;
  private final boolean autoStart;

  private JPKafkaDynamicBatchConsumer(String topic, KafkaListenerContainerFactory<?> kafkaListenerContainerFactory,
                                      BatchMessageListener<K, V> messageListener, boolean autoStart) {
    Assert.notNull(topic, "topic must be not null");
    Assert.notNull(kafkaListenerContainerFactory, "kafkaListenerContainerFactory must be not null");
    Assert.notNull(messageListener, "messageListener must be not null");
    this.topic = topic;
    this.messageListenerContainer = getListenerContainer(kafkaListenerContainerFactory, messageListener, autoStart);
    this.autoStart = autoStart;
  }

  public static <K, V> Builder<K, V> builder(BatchMessageListener<K, V> messageListener) {
    return new Builder<>(messageListener);
  }

  private MessageListenerContainer getListenerContainer(KafkaListenerContainerFactory<?> kafkaListenerContainerFactory,
                                                        BatchMessageListener<K, V> messageListener, boolean autoStart) {
    MessageListenerContainer listenerContainer = kafkaListenerContainerFactory.createContainer(topic);
    listenerContainer.setupMessageListener(messageListener);
    listenerContainer.setAutoStartup(autoStart);
    return listenerContainer;
  }

  /**
   * Признак автозапуска
   */
  @Override
  public boolean isAutoStart() {
    return autoStart;
  }

  /**
   * Запускает слушателя
   */
  @Override
  public void start() {
    synchronized (this) {
      if (messageListenerContainer.isRunning()) {
        return;
      }
      messageListenerContainer.start();
      LOG.debug("Listener \"{}\" started", topic);
    }
  }

  /**
   * Останавливает слушателя
   */
  @Override
  public void stop() {
    synchronized (this) {
      if (messageListenerContainer.isRunning()) {
        messageListenerContainer.stop();
        LOG.debug("Listener \"{}\" stopped", topic);
      }
    }
  }

  /**
   * Ставит слушателя на паузу
   */
  @Override
  public void pause() {
    synchronized (this) {
      if (messageListenerContainer.isRunning()) {
        messageListenerContainer.pause();
        LOG.debug("Listener \"{}\" paused", topic);
      }
    }
  }

  /**
   * Снимает слушателя с паузы
   */
  @Override
  public void resume() {
    synchronized (this) {
      if (messageListenerContainer.isRunning() && messageListenerContainer.isContainerPaused()) {
        messageListenerContainer.resume();
        LOG.debug("Listener \"{}\" resumed", topic);
      }
    }
  }

  /**
   * Топик, к которому подключен данный слушатель
   */
  @Override
  public String getTopic() {
    return topic;
  }

  public static final class Builder<K, V> {
    private final BatchMessageListener<K, V> messageListener;
    private KafkaListenerContainerFactory<?> kafkaListenerContainerFactory;
    private String topic;
    private boolean autoStart = true;

    public Builder(BatchMessageListener<K, V> messageListener) {
      this.messageListener = messageListener;
    }

    public Builder<K, V> kafkaListenerContainerFactory(KafkaListenerContainerFactory<?> kafkaListenerContainerFactory) {
      this.kafkaListenerContainerFactory = kafkaListenerContainerFactory;
      return this;
    }

    public Builder<K, V> topic(String topic) {
      this.topic = topic;
      return this;
    }

    public Builder<K, V> autoStart(boolean autoStart) {
      this.autoStart = autoStart;
      return this;
    }

    public JPKafkaDynamicConsumer<K, V> build() {
      return new JPKafkaDynamicBatchConsumer<>(topic, kafkaListenerContainerFactory, messageListener, autoStart);
    }
  }
}
