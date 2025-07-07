package org.example.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "kafka")
@Getter
@Setter
public class KafkaProperties {
  private String topic;
  //    public String getTopic() { return topic; }
  //    public void setTopic(String topic) { this.topic = topic; }
}
