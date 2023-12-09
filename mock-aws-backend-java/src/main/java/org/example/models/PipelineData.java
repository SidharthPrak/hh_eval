package org.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
// This takes care of setters, getters, and constructor
public class PipelineData implements Serializable {

    @JsonProperty("pipeline_id")
    private String pipeline_id;

    @JsonProperty("inputs")
    private List<InputData> inputs;

    @JsonProperty("configs")
    private List<ConfigData> configs;

    @JsonProperty("trial")
    private Integer trial;


}


