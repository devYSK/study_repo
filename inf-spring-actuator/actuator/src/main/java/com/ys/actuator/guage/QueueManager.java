package com.ys.actuator.guage;

import org.springframework.stereotype.Service;

@Service
public class QueueManager {

	public long getQueueSize() {
		return System.currentTimeMillis();  // queue에 대기중인 데이터의 수를 리턴하는것으로 가정
	}
}