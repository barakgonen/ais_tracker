package org.example.events;

import dk.dma.ais.message.AisMessage;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ExtractedAisMessage extends ApplicationEvent {
    private final AisMessage aisMessage;
    public ExtractedAisMessage(Object source, AisMessage aisMessage) {
        super(source);
        this.aisMessage = aisMessage;
    }
}
