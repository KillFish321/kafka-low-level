package com.example.kafkalowlevel.kafka.service.consumer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PullKafkaConsumerService {

    private final ExecutorService executorService;

    public PullKafkaConsumerService() {
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @PostConstruct
    public void startConsuming() {
        executorService.submit(this::consume);
    }

    @PreDestroy
    public void shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }

    public void consume() {
        Properties consumerProps = new Properties();
        consumerProps.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProps.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "pull-group");
        consumerProps.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false"); // Отключаем автофиксацию смещений
        consumerProps.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps);

        // Подписка на топик
        List<String> topics = Collections.singletonList("my-topic");
        consumer.subscribe(topics);
        // Временное хранилище
        List<ConsumerRecord<String, String>> buffer = new ArrayList<>();
        int batchSize = 5;

        try {
            while (true) {
                // Ручной вызов poll для получения сообщений
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5)); // Увеличиваем таймаут для получения большего количества сообщений

                for (ConsumerRecord<String, String> record : records) {
                    buffer.add(record);
                }
                if (buffer.size() >= batchSize) {
                    for (ConsumerRecord<String, String> record : buffer) {
                        log.info("Pull Consumer - Получено сообщение: value = {}, offset = {}", record.value(), record.offset());
                        // Фиксируем смещения вручную после успешной обработки сообщения
                        commitOffset(consumer, record.topic(), record.partition(), record.offset());
                    }
                    buffer.clear();
                } //else {
//                    log.info("Сообщений в батче {}", buffer.size());
//                }
            }
        } catch (WakeupException e) {
            log.warn("Потребление остановлено", e);
        } catch (Exception e) {
            log.error("Ошибка при потреблении сообщений", e);
        } finally {
            consumer.close(); // Закрываем потребителя
        }
    }

    /**
     * Метод для фиксации смещения вручную
     */
    private void commitOffset(KafkaConsumer<String, String> consumer, String topic, int partition, long offset) {
        OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(offset + 1L); // Смещение увеличивается на единицу после успешного прочтения
        Map<TopicPartition, OffsetAndMetadata> offsetsToCommit = new HashMap<>();
        offsetsToCommit.put(new TopicPartition(topic, partition), offsetAndMetadata);
        consumer.commitSync(offsetsToCommit);
    }
}