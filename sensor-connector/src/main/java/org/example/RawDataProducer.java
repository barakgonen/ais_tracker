package org.example;

import java.time.Instant;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ais.avro.schemas.*;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RawDataProducer {
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final KafkaProducerConfig configProperties;

  public void sendRawDataEvent(RawData rawData) {
    var message = InterfaceEvent.newBuilder().setRawData(rawData);
    produceMessage(message);
  }

  private void produceMessage(InterfaceEvent.Builder eventBuilder) {
    var timeStamp = Instant.now().toEpochMilli();
    eventBuilder.setTimeStamp(timeStamp);
    kafkaTemplate.send(
        configProperties.getRawDataTopic(), UUID.randomUUID().toString(), eventBuilder.build());
  }
}
