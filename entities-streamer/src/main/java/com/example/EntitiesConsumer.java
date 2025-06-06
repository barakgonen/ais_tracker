package com.example;

import com.ais.avro.schemas.InterfaceEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class EntitiesConsumer {
    private final StreamableEntitiesWebSocketHandler streamableEntitiesWebSocketHandler;
    @KafkaListener(topics = "${topic}", groupId = "your-group")
    public void consume(InterfaceEvent event) {
        if (event.getAisMessage() != null) {
            streamableEntitiesWebSocketHandler.sendMessage(event.getAisMessage());
        }
    }
}
