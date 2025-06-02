package org.example;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ais.avro.schemas.InterfaceEvent;
import com.example.application.events.RawDataEvent;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class RawDataConsumer {
  private final StreamableEntitiesProducer producer;
  private final ApplicationEventPublisher publisher;

  @KafkaListener(topics = "${consumerFrom}", groupId = "your-groupa")
  public void consume(InterfaceEvent event) {
    if (event.getAisMessage() != null) {
      producer.sendAisMessage(event.getAisMessage());
    }
    if (event.getRawData() != null) {
      publisher.publishEvent(new RawDataEvent(this, event.getRawData()));
      log.info("Just sent a RawDataEvent");
    }
  }
}
