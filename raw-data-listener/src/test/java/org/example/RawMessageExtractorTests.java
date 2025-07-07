package org.example;

import java.util.List;

import org.example.events.ExtractedAisMessage;
import org.example.events.MessageForExtraction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import dk.dma.ais.message.AisMessage1;

@ExtendWith(MockitoExtension.class)
public class RawMessageExtractorTests {
  @Mock private ApplicationEventPublisher applicationEventPublisher;
  @InjectMocks private RawMessageExtractor rawMessageExtractor;
  @Captor ArgumentCaptor<ExtractedAisMessage> extractedAisMessageArgumentCaptor;

  @Test
  public void testParsingIncomingMessage() {
    // == Arrange
    List<Integer> hex =
        List.of(
            33, 66, 83, 86, 68, 77, 44, 49, 44, 49, 44, 44, 65, 44, 49, 51, 81, 101, 118, 118, 48,
            48, 49, 101, 48, 70, 99, 55, 100, 81, 48, 86, 71, 70, 63, 109, 52, 68, 48, 48, 82, 67,
            44, 48, 42, 49, 69);
    List<Byte> bytes = hex.stream().map(Integer::byteValue).toList();
    MessageForExtraction messageForExtraction = new MessageForExtraction(this, bytes);

    // == Act
    rawMessageExtractor.extractRawMessage(messageForExtraction);
    Mockito.verify(applicationEventPublisher)
        .publishEvent(extractedAisMessageArgumentCaptor.capture());
    // == Assert
    var extractedAisMessage = extractedAisMessageArgumentCaptor.getValue();
    Assertions.assertTrue(extractedAisMessage.getAisMessage() instanceof AisMessage1);
    var aisMessage = (AisMessage1) extractedAisMessage.getAisMessage();
    Assertions.assertEquals(1, aisMessage.getMsgId());
    Assertions.assertEquals(0, aisMessage.getRepeat());
    Assertions.assertEquals(236683000, aisMessage.getUserId());
    Assertions.assertEquals(0, aisMessage.getNavStatus());
    Assertions.assertEquals(-1, aisMessage.getRot());
    Assertions.assertEquals(109, aisMessage.getSog());
    Assertions.assertEquals(0, aisMessage.getPosAcc());
    // Assert position
    Assertions.assertEquals(1599, aisMessage.getCog());
    Assertions.assertEquals(162, aisMessage.getTrueHeading());
    Assertions.assertEquals(10, aisMessage.getUtcSec());
    Assertions.assertEquals(0, aisMessage.getSpecialManIndicator());
    Assertions.assertEquals(0, aisMessage.getSpare());
    Assertions.assertEquals(0, aisMessage.getRaim());
    Assertions.assertEquals(0, aisMessage.getSyncState());
    Assertions.assertEquals(0, aisMessage.getSlotTimeout());
    Assertions.assertEquals(2195, aisMessage.getSubMessage());
    Assertions.assertEquals(57.688048333333334, aisMessage.getPos().getGeoLocation().getLatitude());
    Assertions.assertEquals(4.953156666666667, aisMessage.getPos().getGeoLocation().getLongitude());
  }
}
