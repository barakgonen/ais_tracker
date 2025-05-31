package org.example;

import com.ais.avro.schemas.ConnectionStatus;
import com.ais.avro.schemas.Heartbeat;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        Heartbeat heartbeat = Heartbeat.newBuilder()
                .setStatus(ConnectionStatus.CONNECTED)
                .build();
        SpringApplication.run(Main.class, args);
    }
}
