package mp.jprime.kafka.tests;

import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.kafka.configs.JPKafkaContainerConfig;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.*;

public abstract class KafkaDLQConsumerBaseTest extends JPKafkaContainerConfig {

  protected void createTopics(String bootstrapServers, String topic, int partitions) {
    try (AdminClient adminClient = AdminClient.create(Collections.singletonMap(
        "bootstrap.servers", bootstrapServers
    ))) {
      List<NewTopic> topics = List.of(
          new NewTopic(topic, partitions, (short) 1)
      );
      adminClient.createTopics(topics).all().get();
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }


  protected void deleteTopics(String bootstrapServers, Collection<String> topics) {
    try (AdminClient adminClient = AdminClient.create(Collections.singletonMap(
        "bootstrap.servers", bootstrapServers
    ))) {
      adminClient.deleteTopics(topics).all().get();
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  protected void print(String message) {
    System.out.println(message);
  }

  protected void assertNoDuplicates(Collection<String> collection) {
    if (collection == null || collection.isEmpty()) {
      return;
    }
    Set<String> set = new HashSet<>(collection);
    if (collection.size() != set.size()) {
      throw new IllegalArgumentException(
          "Обнаружены дубликаты: " + findDuplicates(collection)
      );
    }
  }

  protected Set<String> findDuplicates(Collection<String> collection) {
    if (collection == null || collection.isEmpty()) {
      return Collections.emptySet();
    }
    Set<String> seen = new HashSet<>();
    Set<String> duplicates = new HashSet<>();
    for (String item : collection) {
      if (!seen.add(item)) {
        duplicates.add(item);
      }
    }
    return duplicates;
  }
}
