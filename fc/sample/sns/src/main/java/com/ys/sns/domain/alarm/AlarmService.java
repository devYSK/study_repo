package com.ys.sns.domain.alarm;

import com.ys.sns.exception.ErrorCode;
import com.ys.sns.exception.SimpleSnsApplicationException;
import com.ys.sns.domain.user.UserEntity;
import com.ys.sns.domain.alarm.repository.AlarmEntityRepository;
import com.ys.sns.infra.EmitterRepository;
import com.ys.sns.domain.user.repository.UserEntityRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

	public static final String ALARM_NAME = "alarm";

	private final AlarmEntityRepository alarmEntityRepository;
	private final EmitterRepository emitterRepository;
	private final UserEntityRepository userEntityRepository;

	public static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

	public void sendAll(String message) {
		emitterRepository.findAll()
			.forEach(it -> {
					try {
						it.send(SseEmitter.event()
							.id(UUID.randomUUID().toString())
							.name(ALARM_NAME)
							.data(message));
					} catch (IOException exception) {
						throw new SimpleSnsApplicationException(ErrorCode.NOTIFICATION_CONNECT_ERROR);
					}
				}
			);
	}

	public void send(AlarmType type, AlarmArgs args, Integer receiverId) {
		log.info("send!!!!!!!");
		UserEntity userEntity = userEntityRepository.findById(receiverId)
			.orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.USER_NOT_FOUND));
		AlarmEntity entity = AlarmEntity.of(type, args, userEntity);

		alarmEntityRepository.save(entity);

		emitterRepository.get(receiverId)
			.ifPresentOrElse(it -> {
					try {
						it.send(SseEmitter.event()
							.id(entity.getId()
								.toString())
							.name(ALARM_NAME)
							.data(new AlarmNoti("data! " + type + ", ")));
					} catch (IOException exception) {
						emitterRepository.delete(receiverId);
						throw new SimpleSnsApplicationException(ErrorCode.NOTIFICATION_CONNECT_ERROR);
					}
				},
				() -> log.info("No emitter founded")
			);
	}

	public SseEmitter connectNotification(Integer userId) {
		SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
		emitterRepository.save(userId, emitter);
		emitter.onCompletion(() -> emitterRepository.delete(userId));
		emitter.onTimeout(() -> emitterRepository.delete(userId));

		try {
			log.info("send");
			emitter.send(SseEmitter.event()
				.id("id")
				.name(ALARM_NAME)
				.data("connect completed"));
		} catch (IOException exception) {
			throw new SimpleSnsApplicationException(ErrorCode.NOTIFICATION_CONNECT_ERROR);
		}

		return emitter;
	}

	public Collection<SseEmitter> findAll() {
		return this.emitterRepository.findAll();
	}
}
