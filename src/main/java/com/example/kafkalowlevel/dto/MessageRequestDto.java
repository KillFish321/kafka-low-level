package com.example.kafkalowlevel.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageRequestDto {
    private Message message;
    private String topic;
}
