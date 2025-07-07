package org.example.extraction;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class KafkaProducerConfig {

  @Value("${kafka.produce.to.topic}")
  private String produceToTopic;
}
