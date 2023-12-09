package org.example.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PythonServiceRequest {

    private InputData inputData;
    private String codeBlock;
}
