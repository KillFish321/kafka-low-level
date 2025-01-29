package com.example.kafkalowlevel.controller;

import com.example.kafkalowlevel.kafka.utils.MessageGenerateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestKafkaController {

    private final MessageGenerateService producer;

    @GetMapping("/generate")
    public void sendMessageGenerate(){
        producer.toggleScheduler();
    }
}
