package org.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


@Service
public class PythonService {
    /**
     * Routes the Python code exeution service to the actual Python function
     */
    @Value("${services.python.url}")
    private static String pythonServiceUrl;

    @Value("${services.python.fetch-extension}")
    private static String fetch_extension;
    private final RestTemplate restTemplate;

    @Autowired
    public PythonService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public String executeCode(String requestBody){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        return restTemplate.postForObject(pythonServiceUrl+fetch_extension, requestEntity, String.class);
    }
}
