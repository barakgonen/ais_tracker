package com.example;

import com.ais.avro.schemas.AisMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

@Slf4j
public class StreamableEntitiesWebSocketHandler extends TextWebSocketHandler {
    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final LinkedTransferQueue<AisMessage> rawMessages = new LinkedTransferQueue<>();
    private boolean started = false;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        log.info("✅ Client connected: {}", session.getId());

        // Start broadcasting mock data once
        if (!started) {
            started = true;
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    List<Map<String, Object>> ships = generateMockShips();
                    String json = objectMapper.writeValueAsString(ships);

                    for (WebSocketSession s : sessions) {
                        if (s.isOpen()) {
                            s.sendMessage(new TextMessage(json));
                        }
                    }
                } catch (Exception e) {
                    log.error("❌ Failed to send ship data: {}", e.getMessage());
                }
            }, 0, 2, TimeUnit.SECONDS);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        log.info("❌ Client disconnected: {}", session.getId());
    }

    private List<Map<String, Object>> generateMockShips() {
        List<Map<String, Object>> ships = new ArrayList<>();
        while (!rawMessages.isEmpty()) {
            Map<String, Object> shipData = new HashMap<>();
            var aisMessage = rawMessages.remove();
            shipData.put("mmsi", aisMessage.getMmsi());
            shipData.put("lat", aisMessage.getLatitude());
            shipData.put("lon", aisMessage.getLongitude());
            ships.add(shipData);
        }

        return ships;
    }

    public void sendMessage(AisMessage aisMessage) {
        rawMessages.add(aisMessage);
    }
}
