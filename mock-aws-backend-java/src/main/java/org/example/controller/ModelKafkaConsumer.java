package org.example.controller;

import org.example.models.KafkaMessageRequest;
import org.example.models.PipelineData;
import org.example.producer.KafkaProducer;
import org.example.service.PipelineExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pipeline")
public class ModelKafkaConsumer {


    @Autowired
    private PipelineExecutor pipelineExecutor;

    @Autowired
    private KafkaProducer kafkaProducer;


    @PostMapping(value = "/execute")
    public ResponseEntity<String> executePipeline(@RequestBody PipelineData pipelineData){

        pipelineExecutor.executePipeline(pipelineData);
        System.out.println(pipelineData);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/produce")
    public ResponseEntity<String> produce(KafkaMessageRequest kafkaMessageRequest){


        kafkaProducer.sendMessage(kafkaMessageRequest.getTopic(), kafkaMessageRequest.getMessage());

        return new ResponseEntity(HttpStatus.OK);
    }

}
