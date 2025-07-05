package org.example;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.example.events.ExtractedAisMessage;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ais.avro.schemas.AisMessage;
import com.ais.avro.schemas.InterfaceEvent;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class StreamableEntitiesProducer {
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final KafkaProducerConfig configProperties;

  @EventListener
  public void sendMessage(ExtractedAisMessage extractedAisMessage) {
    getAisAvroMessage(extractedAisMessage.getAisMessage())
        .ifPresent(
            aisMessage -> {
              produceMessage(InterfaceEvent.newBuilder().setAisMessage(aisMessage));
              log.info("Produces converted ais message, MMSI: {}", aisMessage.getMmsi());
            });
  }

  private void produceMessage(InterfaceEvent.Builder eventBuilder) {
    var timeStamp = Instant.now().toEpochMilli();
    eventBuilder.setTimeStamp(timeStamp);
    kafkaTemplate.send(
        configProperties.getRawDataTopic(), UUID.randomUUID().toString(), eventBuilder.build());
  }

  private Optional<AisMessage> getAisAvroMessage(dk.dma.ais.message.AisMessage aisMessage) {
    if (aisMessage.getUserId() == 0) {
      log.warn("Got a message with UserID = 0. rejecting");
      return Optional.empty();
    }

    if (aisMessage.getValidPosition() == null
        || aisMessage.getValidPosition().getLatitude() == 0.0
        || aisMessage.getValidPosition().getLongitude() == 0.0) {
      log.warn("Got a message with invalid position. MMSI: {}, rejecting", aisMessage.getUserId());
      return Optional.empty();
    }

    return Optional.of(
        AisMessage.newBuilder()
            .setMmsi(aisMessage.getUserId())
            .setLatitude(aisMessage.getValidPosition().getLatitude())
            .setLongitude(aisMessage.getValidPosition().getLongitude())
            .build());
  }
}
