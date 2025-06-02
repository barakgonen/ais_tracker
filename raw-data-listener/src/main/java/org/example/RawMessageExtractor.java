package org.example;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.example.application.events.MessageForExtraction;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RawMessageExtractor {

  @EventListener
  public void extractRawMessage(MessageForExtraction messageForExtraction) {
    log.info("Got a message!!! in size: {}", messageForExtraction.getBytes().size());
  }
}
