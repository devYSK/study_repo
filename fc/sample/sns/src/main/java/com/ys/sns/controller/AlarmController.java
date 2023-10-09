package com.ys.sns.controller;

import static com.ys.sns.domain.alarm.AlarmService.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ys.sns.configuration.filter.JwtTokenFilter;
import com.ys.sns.domain.user.User;
import com.ys.sns.exception.ErrorCode;
import com.ys.sns.exception.SimpleSnsApplicationException;
import com.ys.sns.utils.ClassUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class AlarmController {

	private final MyEmitterRepository emitterRepository;

	/**
	 * 필터 설정 해야함
	 *
	 * @see JwtTokenFilter
	 */
	@GetMapping(value = "/subscribe")
	public SseEmitter subscribe(Authentication authentication) {
		User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
		final var emitter = connectNotification(user.getId());

		log.info(" on subscribe! User : {}, {}, timeOut Emitter : {}\n",
			user.getId(),
			user.getUsername(),
			getTimeOut(emitter))
		;

		return emitter;
	}

	@GetMapping("/all")
	public void test(@RequestParam(required = false) String data) {
		String tempData = "그냥 보내봄";

		if (!StringUtils.hasText(data)) {
			tempData = "emptyData!";
		} else {
			tempData = data;
		}

		final String sendData = tempData;

		emitterRepository.findAllMap()
			.forEach((userId, sseEmitter) -> {
				try {
					sseEmitter.send(SseEmitter.event()
						.id(UUID.randomUUID()
							.toString())
						.name(ALARM_NAME)
						.data(sendData));

					log.info("data send. data : {}, user :{},  emitter timeOut : {} \n",
						sendData,
						userId,
						getTimeOut(sseEmitter));
				} catch (IOException exception) {
					throw new SimpleSnsApplicationException(ErrorCode.NOTIFICATION_CONNECT_ERROR);
				}
			});

	}

	public SseEmitter connectNotification(Integer userId) {
		SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
		emitterRepository.save(userId, emitter);
		emitter.onCompletion(() -> emitterRepository.delete(userId));
		emitter.onTimeout(() -> emitterRepository.delete(userId));

		try {
			emitter.send(SseEmitter.event()
				.id("id")
				.name("연결 이벤트")
				.data("연결 완료"));
		} catch (IOException exception) {
			throw new SimpleSnsApplicationException(ErrorCode.NOTIFICATION_CONNECT_ERROR);
		}

		return emitter;
	}

	@Slf4j
	@Repository
	@RequiredArgsConstructor
	public static class MyEmitterRepository {
		private Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

		public SseEmitter save(Integer userId, SseEmitter emitter) {
			final String key = getKey(userId);
			emitterMap.put(key, emitter);

			return emitter;
		}

		public void delete(Integer userId) {
			emitterMap.remove(getKey(userId));
		}

		public Optional<SseEmitter> get(Integer userId) {
			SseEmitter result = emitterMap.get(getKey(userId));
			log.info("Get Emitter from Redis {}", result);
			return Optional.ofNullable(result);
		}

		private String getKey(Integer userId) {
			return "emitter:UID:" + userId;
		}

		public Collection<SseEmitter> findAll() {
			return this.emitterMap.values();
		}

		public Map<String, SseEmitter> findAllMap() {
			return this.emitterMap;
		}
	}

	private LocalDateTime getTimeOut(final SseEmitter emitter) {
		long timeoutMillis = emitter.getTimeout();

		LocalDateTime currentDateTime = LocalDateTime.now();
		LocalDateTime expirationDateTime = currentDateTime.plus(Duration.ofMillis(timeoutMillis));

		return expirationDateTime;
	}

}
