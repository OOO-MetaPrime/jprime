package mp.jprime.kafka.consumers;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

public abstract class JPKafkaDeadLetterBatchStringConsumerService extends JPKafkaDeadLetterBatchConsumerService<String, String> {
  private static final Class<StringDeserializer> KEY_DESERIALIZER = StringDeserializer.class;
  private static final Class<StringDeserializer> VALUE_DESERIALIZER = StringDeserializer.class;

  @Override
  protected Class<? extends Deserializer<String>> getKeyDeserializer() {
    return KEY_DESERIALIZER;
  }

  @Override
  protected Class<? extends Deserializer<String>> getValueDeserializer() {
    return VALUE_DESERIALIZER;
  }

}
