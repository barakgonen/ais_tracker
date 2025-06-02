package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final StreamableEntitiesWebSocketHandler handler = new StreamableEntitiesWebSocketHandler();


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new StreamableEntitiesWebSocketHandler(), "/ws/entities")
                .setAllowedOrigins("*"); // allow frontend to connect
    }

    @Bean
    public StreamableEntitiesWebSocketHandler shipHandler() {
        return handler;
    }
}
