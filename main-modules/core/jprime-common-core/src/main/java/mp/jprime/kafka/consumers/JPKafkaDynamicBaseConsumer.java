package mp.jprime.kafka.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.util.Assert;

/**
 * Динамический слушатель
 *
 * @param <K> тип ключа события
 * @param <V> тип значения события
 */
public class JPKafkaDynamicBaseConsumer<K, V> implements JPKafkaDynamicConsumer<K, V> {
  private static final Logger LOG = LoggerFactory.getLogger(JPKafkaDynamicBaseConsumer.class);

  private final String topic;
  private final MessageListenerContainer messageListenerContainer;

  private JPKafkaDynamicBaseConsumer(String topic, KafkaListenerContainerFactory<?> kafkaListenerContainerFactory,
                                     MessageListener<K, V> messageListener, boolean autoStart) {
    Assert.notNull(topic, "topic must be not null");
    Assert.notNull(kafkaListenerContainerFactory, "kafkaListenerContainerFactory must be not null");
    Assert.notNull(messageListener, "messageListener must be not null");
    this.topic = topic;
    this.messageListenerContainer = getListenerContainer(kafkaListenerContainerFactory, messageListener, autoStart);
    if (autoStart) {
      start();
    }
  }

  private MessageListenerContainer getListenerContainer(KafkaListenerContainerFactory<?> kafkaListenerContainerFactory,
                                                        MessageListener<K, V> messageListener, boolean autoStart) {
    MessageListenerContainer listenerContainer = kafkaListenerContainerFactory.createContainer(topic);
    listenerContainer.setupMessageListener(messageListener);
    listenerContainer.setAutoStartup(autoStart);
    return listenerContainer;
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

  public static <K, V> Builder<K, V> builder() {
    return new Builder<>();
  }

  public static final class Builder<K, V> {
    private KafkaListenerContainerFactory<?> kafkaListenerContainerFactory;
    private String topic;
    private MessageListener<K, V> messageListener;
    private boolean autoStart = true;

    public Builder<K, V> kafkaListenerContainerFactory(KafkaListenerContainerFactory<?> kafkaListenerContainerFactory) {
      this.kafkaListenerContainerFactory = kafkaListenerContainerFactory;
      return this;
    }

    public Builder<K, V> topic(String topic) {
      this.topic = topic;
      return this;
    }

    public Builder<K, V> messageListener(MessageListener<K, V> messageListener) {
      this.messageListener = messageListener;
      return this;
    }

    public Builder<K, V> autoStart(boolean autoStart) {
      this.autoStart = autoStart;
      return this;
    }

    public JPKafkaDynamicConsumer<K, V> build() {
      return new JPKafkaDynamicBaseConsumer<>(topic, kafkaListenerContainerFactory, messageListener, autoStart);
    }
  }
}
