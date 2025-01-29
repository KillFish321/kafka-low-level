package com.example.kafkalowlevel.kafka.service.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {

    @KafkaListener(topics = "my-topic", groupId = "example-group")
    public void listen(ConsumerRecord<String, String> record) {
        log.info("Получено сообщение: value = {}, offset = {}", record.value(), record.offset());
    }
}

