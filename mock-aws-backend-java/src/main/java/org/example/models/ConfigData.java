package org.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ConfigData implements Serializable {

    @JsonProperty("model")
    private String model;
    @JsonProperty("temperature")
    private String temperature;
}
