package org.example;

import com.ais.avro.schemas.AisMessage;
import com.ais.avro.schemas.ConnectionStatus;
import com.ais.avro.schemas.Heartbeat;
import com.ais.avro.schemas.InterfaceEvent;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class StreamableEntitiesProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaProducerConfig configProperties;


    public void sendMessage() {

        Heartbeat heartbeat = Heartbeat.newBuilder()
                .setStatus(ConnectionStatus.CONNECTED)
                .build();
        var message = InterfaceEvent.newBuilder()
                .setHeartBeat(heartbeat);
        produceMessage(message);
        System.out.println("Produced: " + message);
    }

    public void sendAisMessage(AisMessage aisMessage) {
        var message = InterfaceEvent.newBuilder()
                .setAisMessage(aisMessage);
        produceMessage(message);
    }

    private void produceMessage(InterfaceEvent.Builder eventBuilder) {
        var timeStamp = Instant.now().toEpochMilli();
        eventBuilder.setTimeStamp(timeStamp);
        kafkaTemplate.send(configProperties.getRawDataTopic(), UUID.randomUUID().toString(), eventBuilder.build());
    }
}
