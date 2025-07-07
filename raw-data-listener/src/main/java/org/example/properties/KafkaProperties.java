package org.example.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka")
@Getter
@Setter
public class KafkaProperties {
    private String topic;
//    public String getTopic() { return topic; }
//    public void setTopic(String topic) { this.topic = topic; }
}
