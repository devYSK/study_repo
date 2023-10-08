package com.ys.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class KafkaConsumerApplicationTests extends IntegrationTest {

	@Autowired
	private KafkaProducerService kafkaProducerService;

	@MockBean
	private KafkaConsumerService kafkaConsumerService;

	@Test
	void kafkaSendAndConsumeTest() {
		String topic = "test-topic";
		String expectValue = "expect-value";

		kafkaProducerService.send(topic, expectValue);

		var stringCaptor = ArgumentCaptor.forClass(String.class);

		Mockito.<KafkaConsumerService>verify(kafkaConsumerService, timeout(5000)
				   .times(1))
			   .process(stringCaptor.capture());

		Assertions.assertEquals(expectValue, stringCaptor.getValue());
	}

}