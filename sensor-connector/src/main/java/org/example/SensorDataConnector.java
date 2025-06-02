package org.example;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.function.Consumer;

import org.example.config.NetworkConfig;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ais.avro.schemas.RawData;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.reader.AisReader;
import dk.dma.ais.reader.AisReaders;
import dk.dma.ais.sentence.CommentBlock;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class SensorDataConnector {
  private final RawDataProducer rawDataProducer;
  private final NetworkConfig networkConfig;

  @Scheduled(fixedRate = 1000)
  public void fetchAisData() throws InterruptedException {
    AisReader reader =
        AisReaders.createReader(networkConfig.getHostName(), networkConfig.getPort());
    reader.registerPacketHandler(
            packet -> {
              try {
                AisMessage message = packet.getAisMessage();
                byte[] rawData = packet.toByteArray();
                RawData avroRawData = RawData.newBuilder().setData(ByteBuffer.wrap(rawData)).build();
                rawDataProducer.sendRawDataEvent(avroRawData);
                log.info("Produced raw data message, size: {}", rawData.length);
                // Now avroBytes is the serialized Avro message
                //                    System.out.println("Avro serialized data size: " +
                // avroBytes.length + ", origin: " + rawData.length);
              } catch (AisMessageException | SixbitException e) {
                // Handle
                return;
              }
              // Alternative returning null if no valid AIS message
              AisMessage message = packet.tryGetAisMessage();

              Date timestamp = packet.getTimestamp();
              CommentBlock cb = packet.getVdm().getCommentBlock();
            });
    reader.start();
    reader.join();
  }
}
