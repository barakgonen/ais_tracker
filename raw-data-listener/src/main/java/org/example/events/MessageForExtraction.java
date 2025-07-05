package org.example.events;

import java.util.List;

import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class MessageForExtraction extends ApplicationEvent {

  private List<Byte> bytes;

  public MessageForExtraction(Object source, List<Byte> bytes) {
    super(source);
    this.bytes = bytes;
  }
}
