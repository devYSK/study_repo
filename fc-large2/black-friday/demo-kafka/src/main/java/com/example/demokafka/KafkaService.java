package com.example.demokafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void publish() {
        kafkaTemplate.send("topic1", "message sent (topic1)");
    }

    @KafkaListener(topics = "topic1", groupId = "testgroup")
    public void consume(String message) {
        System.out.println("consumed: " + message);
    }
}
