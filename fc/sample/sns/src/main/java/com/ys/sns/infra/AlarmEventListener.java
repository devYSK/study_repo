package com.ys.sns.infra;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.ys.sns.domain.alarm.AlarmEvent;
import com.ys.sns.domain.alarm.AlarmService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmEventListener {

	private final AlarmService alarmService;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void consumeNotification(AlarmEvent event) {
		log.info("Consume the event {}", event);
		alarmService.send(event.getType(), event.getArgs(), event.getReceiverUserId());
	}

}
