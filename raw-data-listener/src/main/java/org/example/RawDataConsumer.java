package org.example;

import com.ais.avro.schemas.InterfaceEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class RawDataConsumer {
    private final StreamableEntitiesProducer producer;

    @KafkaListener(topics = "${consumerFrom}", groupId = "your-group")
    public void consume(InterfaceEvent event) {
        if (event.getAisMessage() != null) {
            producer.sendAisMessage(event.getAisMessage());
        }
    }
}
