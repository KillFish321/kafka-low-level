package com.example.kafkalowlevel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KafkaLowLevelApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaLowLevelApplication.class, args);
    }

}
