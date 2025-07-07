package org.example.events;

import org.springframework.context.ApplicationEvent;

import dk.dma.ais.message.AisMessage;
import lombok.Getter;

@Getter
public class ExtractedAisMessage extends ApplicationEvent {
  private final AisMessage aisMessage;
  private final String msgSource;

  public ExtractedAisMessage(Object source, AisMessage aisMessage, String msgSource) {
    super(source);
    this.aisMessage = aisMessage;
    this.msgSource = msgSource;
  }
}
