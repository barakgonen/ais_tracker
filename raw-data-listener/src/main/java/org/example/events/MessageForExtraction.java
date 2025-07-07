package org.example.events;

import java.util.List;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class MessageForExtraction extends ApplicationEvent {

  private final String messageSource;
  private List<Byte> bytes;

  public MessageForExtraction(Object source, List<Byte> bytes, String messageSource) {
    super(source);
    this.bytes = bytes;
    this.messageSource = messageSource;
  }
}
