package org.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class KafkaMessageRequest {
    @JsonProperty("topic")
    private String topic;

    @JsonProperty("message")
    private String message;

}
