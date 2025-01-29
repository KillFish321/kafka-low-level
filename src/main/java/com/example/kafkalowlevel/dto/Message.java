package com.example.kafkalowlevel.dto;

import lombok.Builder;
import lombok.Getter;
@Getter
@Builder
public class Message {
    private Integer id;
    private String name;
}
