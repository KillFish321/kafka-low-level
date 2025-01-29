package com.example.kafkalowlevel.kafka.service.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PullKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(PullKafkaConsumer.class);

    public void consume() {
        // Конфигурация для pull-потребителя
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.31.211.163:9092");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "pull-group");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps);

        // Подписка на топик
        consumer.subscribe(Collections.singletonList("pull-topic"));

        try {
            while (true) {
                // Ручной вызов poll для получения сообщений
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                records.forEach(record -> log.info("Pull Consumer - Получено сообщение: value = {}, offset = {}", record.value(), record.offset()));
            }
        } catch (Exception e) {
            log.error("Ошибка при потреблении сообщений", e);
        } finally {
            consumer.close();
        }
    }
}

