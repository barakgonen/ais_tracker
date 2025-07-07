package org.example;

import java.io.*;
import java.util.function.Consumer;

import org.example.events.ExtractedAisMessage;
import org.example.events.MessageForExtraction;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.reader.AisReaders;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class RawMessageExtractor {

  private ApplicationEventPublisher applicationEventPublisher;

  @EventListener
  public void extractRawMessage(MessageForExtraction messageForExtraction) {
    log.info("Got an event: {}", messageForExtraction);
    // Got a self contained message, now need to see if we can build an AIS message from it
    var receivedBytes = messageForExtraction.getBytes();
    byte[] byteArray = new byte[receivedBytes.size()];
    for (int i = 0; i < receivedBytes.size(); i++) {
      byteArray[i] = receivedBytes.get(i);
    }
    InputStream inputStream = new ByteArrayInputStream(byteArray);
    byte b = byteArray[2];

    int messageId = (b & 0xFC) >> 2;
    var aisMessage = AisReaders.createReaderFromInputStream(inputStream);
    aisMessage.registerHandler(
        new Consumer<>() {
          @Override
          public void accept(AisMessage aisMessage) {
            applicationEventPublisher.publishEvent(
                new ExtractedAisMessage(this, aisMessage, messageForExtraction.getMessageSource()));
          }
        });
    aisMessage.start();
    try {
      aisMessage.join();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
