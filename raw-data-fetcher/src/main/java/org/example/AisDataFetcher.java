package org.example;

import com.ais.avro.schemas.AisMessage;
import dk.dma.ais.reader.AisReader;
import dk.dma.ais.reader.AisReaders;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class AisDataFetcher {
    private final RawDataProducer rawDataProducer;

    @Scheduled(fixedRate = 1000)
    public void fetchAisData() throws InterruptedException {
        AisReader reader = AisReaders.createReader("153.44.253.27", 5631);
        reader.registerHandler(aisMessage -> {
            extractMessageField(aisMessage).ifPresent(rawDataProducer::sendAisMessage);
        });
        reader.start();
        reader.join();
    }

    private Optional<AisMessage> extractMessageField(dk.dma.ais.message.AisMessage aisMessage) {
        if (aisMessage.getUserId() == 0) {
            log.warn("Rejecting message with ID / mmsi which is 0");
            return Optional.empty();
        }

        if (aisMessage.getValidPosition() == null || aisMessage.getValidPosition().getLongitude() == 0 || aisMessage.getValidPosition().getLatitude() == 0) {
            log.warn("Rejecting message with id: {} because the position data is null", aisMessage.getUserId());
            return Optional.empty();
        }

        int mmsi = aisMessage.getUserId();
        double lat = aisMessage.getValidPosition().getLatitude();
        double longitude = aisMessage.getValidPosition().getLongitude();

        AisMessage aisMessage1 = AisMessage.newBuilder()
                .setMmsi(mmsi)
                .setLatitude(lat)
                .setLongitude(longitude)
                .build();

        return Optional.of(aisMessage1);
    }
}
