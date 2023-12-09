package org.example.service;

import antlr.PythonCodeGenerator;
import org.example.models.*;
import org.example.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class PipelineExecutor {


    @Value("${kafka-topics.producers.retry.topic-name}")
    private static String retryTopic;

    @Value("${kafka-topics.producers.metric.topic-name}")
    private static String metricTopic;

    @Value("${pipeline.thread-count}")
    private static Integer threadCount;

    @Autowired
    private static KafkaProducer kafkaProducer;

    @Autowired
    private static PythonService pythonService;



    private static void singleInputExecution(String pipelineId, InputData input, String codeBlock) {
        /**
         * Processes output from python service and routes to MetricServiceimplements retry
         * Implements retry mechanism
         */
        try {
            // Let us make an API call to a python service for python code exec.
            // Alternate - Jython exists, but would be faster to do the Python processing
            // Kept synchronous for now (number of threads in this service should match
            // number of worker threads in python service )
            String output = pythonService.executeCode(PythonServiceRequest
                    .builder()
                    .inputData(input)
                    .codeBlock(codeBlock)
                    .build()
                    .toString());

            kafkaProducer.sendMessage(metricTopic,
                    MetricKafkaRequest.builder()
                        .requestId(pipelineId)
                        .input(input)
                        .output(output)
                        .build().toString()
            );
        }  catch (Exception e){
            System.err.println("Task " + input.getTopic() + " interrupted");
            kafkaProducer.sendMessage(retryTopic,
                    MetricKafkaRequest.builder()
                            .requestId(pipelineId)
                            .input(input)
                            .build().toString());
        }
    }

    private static List<CompletableFuture<Void>> createTasksAsync(String pipelineId, List<InputData> inputList, ExecutorService executor, String codeBlock) {
        /**
         * Map inputs to functions and creates a completable futures list
         */
        return inputList.stream()
                .map(input -> CompletableFuture.runAsync(() -> singleInputExecution(pipelineId, input, codeBlock), executor))
                .collect(Collectors.toList());
    }

    public void executePipeline(PipelineData pipelineData){
        /**
         * Create thread pool and invoke asynchronous function calls
         */

        // TODO: Code block to be acquired from mongodb.
        String codeBlock  = "";

        ExecutorService executor = Executors.
                                        newFixedThreadPool(threadCount);

        List<CompletableFuture<Void>> futures = createTasksAsync(pipelineData.getPipeline_id(),
                                                                 pipelineData.getInputs(),
                                                                 executor,
                                                                 codeBlock);
        CompletableFuture<Void> allTasks = CompletableFuture
                                                .allOf(futures.toArray(new CompletableFuture[0]));

        allTasks.thenRun(() -> {
            System.out.println("All tasks completed");
        });

        try {
            allTasks.join();
        } catch (CompletionException ex) {
            // No need to process the failed threads as retry already takes care of that
            System.out.println("Exception: " + ex.getCause().getMessage());
        } finally {
            executor.shutdown();
        }
    }
}
