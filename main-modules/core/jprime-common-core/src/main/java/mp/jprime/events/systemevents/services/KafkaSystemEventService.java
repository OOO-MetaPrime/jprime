package mp.jprime.events.systemevents.services;

import mp.jprime.events.systemevents.JPSystemApplicationEvent;
import mp.jprime.events.systemevents.JPSystemEvent;
import mp.jprime.events.systemevents.json.JsonJPSystemEvent;
import mp.jprime.events.systemevents.json.services.JsonJPSystemEventConvertor;
import mp.jprime.json.services.JPKafkaJsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.io.IOException;

@EnableKafka
@Lazy(value = false)
@Service
@ConditionalOnProperty(value = "jprime.events.systemevents.kafka.enabled", havingValue = "true")
public class KafkaSystemEventService implements SystemEventPublisher {
  private static final Logger LOG = LoggerFactory.getLogger(KafkaSystemEventService.class);

  @Value("${jprime.events.systemevents.kafka.kafkaServers:}")
  private String bootstrapAddress;
  @Value("${jprime.events.systemevents.kafka.kafkaTopic:}")
  private String topic;

  private KafkaTemplate<String, String> kafkaTemplate;

  private ApplicationEventPublisher eventPublisher;

  private JsonJPSystemEventConvertor jsonJPSystemEventConvertor;

  private JPKafkaJsonMapper jpKafkaJsonMapper;

  @Autowired
  private void setJPKafkaJsonMapper(JPKafkaJsonMapper jpKafkaJsonMapper) {
    this.jpKafkaJsonMapper = jpKafkaJsonMapper;
  }

  @Autowired
  private void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  @Autowired
  private void setJsonJPSystemEventConvertor(JsonJPSystemEventConvertor jsonJPSystemEventConvertor) {
    this.jsonJPSystemEventConvertor = jsonJPSystemEventConvertor;
  }

  @Autowired(required = false)
  @Qualifier("kafkaSystemEventsTemplate")
  private void setKafkaTemplate(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @KafkaListener(topics = "${jprime.events.systemevents.kafka.kafkaTopic}",
      containerFactory = "kafkaSystemEventsContainerFactory")
  public void listen(String json) {
    try {
      eventPublisher.publishEvent(JPSystemApplicationEvent.from(toEvent(json)));
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
  }

  @Override
  public void publishEvent(JPSystemEvent event) {
    if (kafkaTemplate == null) {
      return;
    }
    try {
      String msg = toJSON(event);
      kafkaTemplate
          .send(topic, msg)
          .addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
            }

            @Override
            public void onFailure(Throwable e) {
              LOG.error("Unable to send message=[" + msg + "] due to : " + e.getMessage(), e);
            }
          });
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
  }


  private String toJSON(JPSystemEvent event) {
    return jpKafkaJsonMapper.toString(jsonJPSystemEventConvertor.toJsonJPSystemEvent(event));
  }

  private JPSystemEvent toEvent(String string) throws IOException, SecurityException {
    return jsonJPSystemEventConvertor.toJPSystemEvent(jpKafkaJsonMapper.getObjectMapper().readValue(string, JsonJPSystemEvent.class));
  }
}
