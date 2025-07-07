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
  private final ApplicationEventPublisher publisher;

  @KafkaListener(topics = "${consume-from}", groupId = "your-groupa")
  public void consume(String key, InterfaceEvent event) {
    log.info("Got an event from kafka queue, key: {}", key);
    if (event.getRawData() != null) {
      publisher.publishEvent(new RawDataEvent(this, event.getRawData()));
    }
  }
}
