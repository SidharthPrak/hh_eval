package org.example.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MetricKafkaRequest {
    private String requestId;

    private InputData input;

    private String output;
}
