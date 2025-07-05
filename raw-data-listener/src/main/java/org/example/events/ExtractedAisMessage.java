package org.example.events;

import org.springframework.context.ApplicationEvent;

import dk.dma.ais.message.AisMessage;
import lombok.Getter;

@Getter
public class ExtractedAisMessage extends ApplicationEvent {
  private final AisMessage aisMessage;

  public ExtractedAisMessage(Object source, AisMessage aisMessage) {
    super(source);
    this.aisMessage = aisMessage;
  }
}
