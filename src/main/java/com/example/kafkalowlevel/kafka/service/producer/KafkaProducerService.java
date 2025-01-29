package com.example.kafkalowlevel.kafka.service.producer;

import com.example.kafkalowlevel.dto.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;

    public void sendMessage(String topic, Message message) {
        try {
            String serializedMessage = mapper.writeValueAsString(message);
            kafkaTemplate.send(topic, serializedMessage);
            log.info("В топик \"{}\" отправлено сообщение: {}", topic, serializedMessage);
        } catch (Exception e) {
            log.error("Не удалось отправить сообщение");
            throw new RuntimeException();
        }
    }
}
