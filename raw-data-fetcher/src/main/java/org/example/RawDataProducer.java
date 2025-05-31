package org.example;

import com.ais.avro.schemas.ConnectionStatus;
import com.ais.avro.schemas.Heartbeat;
import com.ais.avro.schemas.InterfaceEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RawDataProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaProducerConfig configProperties;


    @Scheduled(fixedRate = 1000)
    public void sendMessage() {
        var timeStamp = Instant.now().toEpochMilli();
        Heartbeat heartbeat = Heartbeat.newBuilder()
                .setStatus(ConnectionStatus.CONNECTED)
                .build();
        var message = InterfaceEvent.newBuilder()
                .setTimeStamp(timeStamp)
                .setHeartBeat(heartbeat)
                .build();

        kafkaTemplate.send(configProperties.getRawDataTopic(), UUID.randomUUID().toString(), message);
        System.out.println("Produced: " + message);
    }
}
