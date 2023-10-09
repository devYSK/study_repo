package com.ys.sns.controller;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ys.sns.domain.alarm.AlarmArgs;
import com.ys.sns.domain.alarm.AlarmEvent;
import com.ys.sns.domain.alarm.AlarmType;
import com.ys.sns.domain.user.UserEntity;
import com.ys.sns.domain.user.repository.UserEntityRepository;
import com.ys.sns.domain.alarm.AlarmService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api-dev/v1")
@RequiredArgsConstructor
public class DevController {

	private final AlarmService notificationService;
	private final UserEntityRepository userEntityRepository;
	// private final AlarmProducer alarmProducer;
	private final ApplicationEventPublisher eventPublisher;

	@GetMapping("/notification")
	public void test(@RequestParam String data) {

		String sendData = data;

		if (!StringUtils.hasText(sendData)) {
			sendData = "emptyData!";
		}

		notificationService.findAll()
			.forEach(it -> {
				System.out.println("\n " + it.toString());
				notificationService.sendAll(data);
			});

		// userEntityRepository.findAll()
		// 	.forEach(user -> notificationService.send(AlarmType.NEW_LIKE_ON_POST, new AlarmArgs(0, 0), user.getId()));

		// UserEntity entity = userEntityRepository.findById(5).orElseThrow();
		// notificationService.send(AlarmType.NEW_LIKE_ON_POST, new AlarmArgs(0, 0), entity.getId());
	}

	@GetMapping("/send")
	public void send() {
		// alarmProducer.send(new AlarmEvent(AlarmType.NEW_LIKE_ON_POST, new AlarmArgs(0, 0), 5));

		eventPublisher.publishEvent(new AlarmEvent(AlarmType.NEW_LIKE_ON_POST, new AlarmArgs(0, 0), 5));
	}

}
