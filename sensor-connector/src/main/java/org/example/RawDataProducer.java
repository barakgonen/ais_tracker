package org.example;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ais.avro.schemas.*;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RawDataProducer {
  private static final Logger log = LoggerFactory.getLogger(RawDataProducer.class);
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final KafkaProducerConfig configProperties;

  public void sendRawDataEvent(String key, RawData data) {
    var value = InterfaceEvent.newBuilder().setRawData(data);
    var timeStamp = Instant.now().toEpochMilli();
    value.setTimeStamp(timeStamp);
    produceMessage(key, value.build());
  }

  private void produceMessage(String key, InterfaceEvent value) {
    kafkaTemplate.send(configProperties.getRawDataTopic(), key, value);
    log.info(
        "Sent message on topic: {}, key: {}, value: {}",
        configProperties.getRawDataTopic(),
        key,
        value);
  }
}
