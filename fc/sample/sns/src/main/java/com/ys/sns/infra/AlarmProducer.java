package com.ys.sns.infra;

import lombok.extern.slf4j.Slf4j;

@Slf4j
// @Component
public class AlarmProducer {
	//
	// private final KafkaTemplate<String, AlarmEvent> alarmEventKafkaTemplate;
	//
	// @Value("${spring.kafka.topic.notification}")
	// private String topic;
	//
	// public AlarmProducer(
	// 	@Qualifier("alarmEventKafkaTemplate") final KafkaTemplate<String , AlarmEvent> alarmEventKafkaTemplate) {
	// 	this.alarmEventKafkaTemplate = alarmEventKafkaTemplate;
	// }
	//
	// public void send(AlarmEvent event) {
	// 	alarmEventKafkaTemplate.send(topic, event.getReceiverUserId().toString(), event);
	// 	log.info("send fin");
	// }

}
