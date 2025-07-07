package org.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class KafkaProducerConfig {

  @Value("${produce-to}")
  private String produceToTopic;
  @Value("${interface-name}")
  private String interfaceName;
}
