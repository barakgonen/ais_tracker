package org.example;

import java.time.Instant;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ais.avro.schemas.*;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RawDataProducer {
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
  }
}
