package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.ByteBuffer;

import org.example.config.NetworkConfig;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ais.avro.schemas.RawData;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class SensorDataConnector {
  private final RawDataProducer rawDataProducer;
  private final NetworkConfig networkConfig;

  @Scheduled(fixedRate = 1000)
  public void fetchData() {
    try (Socket socket = new Socket(networkConfig.getHost(), networkConfig.getPort());
        BufferedReader reader =
            new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

      String line;
      while ((line = reader.readLine()) != null) {
        log.debug("Got new data: {}", line);
        RawData rawDataEvent =
            RawData.newBuilder().setData(ByteBuffer.wrap(line.getBytes())).build();
        rawDataProducer.sendRawDataEvent(rawDataEvent);
      }
    } catch (Exception e) {
      log.error(
          "Caught an exception during trying to connect to host: {}, on port: {}, exception: ",
          networkConfig.getHost(),
          networkConfig.getPort(),
          e);
    }
  }
}
