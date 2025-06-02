package org.example.extraction;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.example.events.MessageForExtraction;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.ais.avro.schemas.RawData;
import com.example.application.events.RawDataEvent;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class MessagesStreamSplitter {
  private static final String DELIMITER =
      "!"; // TODO: pass it from the specific implementation, not static assign

  private final ConcurrentLinkedQueue<Byte> bytes = new ConcurrentLinkedQueue<>();
  private final ApplicationEventPublisher publisher;

  @EventListener
  public void extractMessages(RawDataEvent rawDataEvent) {
    // First adding bytes from the message
    handleRawDataMessage(rawDataEvent.getRawData());

    // starting to split messages
    splitMessages();
  }

  private void splitMessages() {
    log.info("Size of bytes: {}", bytes.size());
    // Stage 1: lets split the message
    var delimiterBytes = DELIMITER.getBytes();
    boolean isMessageFound = false;

    // 1.1 find first delimiter
    while (!bytes.isEmpty()) {
      while (!isMessageFound) {
        var firstByte = bytes.poll();
        if (firstByte != null) {
          // need to check if it's inside the delimiterBytes
          if (delimiterBytes[0] == firstByte) {
            log.info("Found a beginning of message header!");
            if (delimiterBytes.length > 1) {
              log.info("Delimiter is longer than 1, need to process");
              for (int i = 1; i < delimiterBytes.length; i++) {
                firstByte = bytes.poll();
                var inDelimiterByte = delimiterBytes[i];
                if (inDelimiterByte != firstByte) {
                  log.error("Not equals");
                  continue;
                }
              }
            }
            isMessageFound = true;
          }
        }
      }

      // Now we have pointed
      List<Byte> byteList = new ArrayList<>();
      while (!bytes.isEmpty() && bytes.peek().byteValue() != delimiterBytes[0]) {
        byteList.add(bytes.poll());
      }

      publisher.publishEvent(new MessageForExtraction(this, byteList));
      // Getting ready to next message
      isMessageFound = false;
    }
    // 1.2 until next delimiter not found, take another byte out

    // 1.3 now you should have a full message
  }

  private void handleRawDataMessage(RawData rawData) {
    ByteBuffer buffer = rawData.getData();
    byte[] dataBytes = new byte[buffer.remaining()];
    buffer.get(dataBytes);
    for (byte dataByte : dataBytes) {
      bytes.add(dataByte);
    }
  }
}
