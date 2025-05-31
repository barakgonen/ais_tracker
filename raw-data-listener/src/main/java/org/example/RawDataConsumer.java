package org.example;

import com.ais.avro.schemas.InterfaceEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RawDataConsumer {
    @KafkaListener(topics = "raw-data-event", groupId = "your-group")
    public void consume(InterfaceEvent event) {
        System.out.println("Received: " + event);
    }
}
