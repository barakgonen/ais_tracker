package org.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class NetworkConfig {
  @Value("${hostName}")
  private String hostName;

  @Value("${port}")
  private Integer port;
}
