package org.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.io.Serializable;
@Data
public class InputData implements Serializable {

    @Data
    public class InputMetadata {
        @JsonProperty("importance")
        private Float importance;
    }

    @JsonProperty("topic")
    private String topic;

    @JsonProperty("input_metadata")
    private InputMetadata inputMetadata;

}