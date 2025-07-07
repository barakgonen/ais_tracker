package org.example;

import com.ais.avro.schemas.RawData;
import jakarta.annotation.PostConstruct;
import org.example.config.NetworkConfig;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.ByteBuffer;

@Service
@Slf4j
@AllArgsConstructor
public class SensorDataConnector {

  private final RawDataProducer rawDataProducer;
  private final NetworkConfig networkConfig;

  @Scheduled(fixedRate = 100)
  public void fetchData() {
    log.info("Trying to connect to: host: {}, port: {}", networkConfig.getHost(), networkConfig.getPort());
    try (Socket socket = new Socket(networkConfig.getHost(), networkConfig.getPort());
         BufferedReader reader =
                 new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

      String line;
      while ((line = reader.readLine()) != null) {
        log.debug("Got new data: {}", line);
        RawData rawDataEvent =
                RawData.newBuilder().setData(ByteBuffer.wrap(line.getBytes())).build();
        rawDataProducer.sendRawDataEvent(networkConfig.getHost(), rawDataEvent);
      }
    } catch (Exception e) {
      log.error(
              "Caught an exception during trying to connect to host: {}, on port: {}, exception: ",
              networkConfig.getHost(),
              networkConfig.getPort(),
              e);
    }
    log.info("Connection is damaged, since we got null data from socket, re-establishing");
  }
}
