package com.example.kafkalowlevel.kafka.utils;

import com.example.kafkalowlevel.dto.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageGenerateService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;
    private static Integer numberMessage = 1;
    private boolean schedulerEnabled = false;

    // Метод для переключения рубильника
    public void toggleScheduler() {
        schedulerEnabled = !schedulerEnabled;
    }

    @Scheduled(fixedRate = 3000)
    private void sendMessageGenerate() throws JsonProcessingException {
        if (!schedulerEnabled){
            return;
        }
        String serializedMessage = mapper.writeValueAsString(messageBuilder());
        kafkaTemplate.send("my-topic", serializedMessage);
    }

    private Message messageBuilder() {
        return Message.builder()
                .id(numberMessage)
                .name("R"+ numberMessage + "D" + numberMessage++)
                .build();
    }
}
