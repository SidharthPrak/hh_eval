package org.example.controller;

import org.example.service.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

@RestController
//@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/kafka_push")
public class SampleApi {

    @Autowired
    private EmailSender emailSender;

    @PostMapping(value = "/metrics")
    public ResponseEntity<String> pushMetrics(@RequestBody Object object){
        System.out.println(object);
        try {
            emailSender.send(object.toString());
        } catch (Exception e){
            System.out.println("Unable to send email");
        }
        return new ResponseEntity<String>(
                "object",
                HttpStatus.OK);
    }
}
