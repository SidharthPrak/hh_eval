package org.example.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.models.PipelineData;
import org.example.service.PipelineExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PipelineConsumer {

    @Autowired
    private PipelineExecutor pipelineExecutor;

    @KafkaListener(topics = "${kafka-topics.consumers.pipeline.topic-name}", groupId = "${kafka-topics.consumers.pipeline.group-id}")
    public void listen(String message) throws JsonProcessingException {
        System.out.println("Received message: " + message);
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            PipelineData pipelineData = objectMapper.readValue(message, PipelineData.class);

            pipelineExecutor.executePipeline(pipelineData);
            System.out.println(pipelineData);
        } catch(Exception e){
            System.out.println(e.getMessage());

        }
    }

}
