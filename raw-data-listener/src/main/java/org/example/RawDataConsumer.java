package org.example;

import com.ais.avro.schemas.InterfaceEvent;
import com.example.application.events.RawDataEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RawDataConsumer {
  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;

  @Value("${kafka.consume.from.topic}")
  private String topic;

  public RawDataConsumer(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @KafkaListener(topics = "${kafka.consume.from.topic}", groupId = "my-consumer-group")
  public void consume(InterfaceEvent event) {
    log.info("Got an event from kafka queue, key: ");
    if (event.getRawData() != null) {
      applicationEventPublisher.publishEvent(new RawDataEvent(this, event.getRawData()));
    }
  }
}
