package com.example.kafkalowlevel.controller;

import com.example.kafkalowlevel.dto.MessageRequestDto;
import com.example.kafkalowlevel.kafka.service.producer.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KafkaController {

    private final KafkaProducerService producer;

    @PostMapping("/send")
    public void sendMessage(@RequestBody MessageRequestDto request) {
        producer.sendMessage(request.getTopic(), request.getMessage());
    }
}
