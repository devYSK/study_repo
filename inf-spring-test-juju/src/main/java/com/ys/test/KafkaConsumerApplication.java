package com.ys.test;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class KafkaConsumerApplication {

	private final KafkaConsumerService kafkaConsumerService;

	@Bean
	public NewTopic topic() {
		return TopicBuilder.name("test-topic")
						   .build();
	}

	@KafkaListener(id = "test-id-1", topics = "test-topic")
	public void listen(String message) {
		System.out.println("listen!");
		kafkaConsumerService.process(message);
	}

}