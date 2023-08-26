package com.example.youthcon.handson;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import com.example.youthcon.preparation.*;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class CommentService {

	private final HashMap<String, Set<SseEmitter>> container = new HashMap<>();

	/**
	 * 새로운 Emitter를 생성
	 * 전송할 이벤트를 작성
	 * 작성한 이벤트를 생성한 이미터에 전송
	 * 아티클과 연결된 이미터 컨테이너를 생성
	 *
	 * @param articleId
	 * @return
	 */
	public SseEmitter connect(String articleId) {

		// 타임아웃이 너무 길면 서버에서 불필요한 커넥션을 관리해야함
		// 너무 짧으면 재연결 요청이 증가하는 단점이 있다.
		final SseEmitter sseEmitter = new SseEmitter(300000L); // 5분

		// 만료된 Emitter가 있을 수 있으므로 만료 콜백을 넣어줘야 한다. 스스로 구현해보기
		// sseEmitter.onCompletion(() -> );

		final SseEmitter.SseEventBuilder connect = SseEmitter.event()
															 .name("connect")
															 .data("connected!")
															 .reconnectTime(3000);

		sendEvent(sseEmitter, connect);

		// 아티클과 연결된 이미터 컨테이너를 생성
		final Set<SseEmitter> sseEmitters = container.getOrDefault(articleId, new HashSet<>());
		sseEmitters.add(sseEmitter);

		container.put(articleId, sseEmitters);

		return sseEmitter;
	}

	private static void sendEvent(SseEmitter emitter, SseEmitter.SseEventBuilder connect) {
		try {
			emitter.send(connect);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void sendComment(Comment comment, String articleId) {
		// 아티클과 연결된 이미터들 가져오기
		// 가져온 이미터들에게 댓글을 전송하기

		final Set<SseEmitter> sseEmitters = this.container.getOrDefault(articleId, new HashSet<>());
		final SseEmitter.SseEventBuilder newComment = SseEmitter.event()
																.name("newComment")
																.data(comment)
																.reconnectTime(3000);
		sseEmitters.forEach(
			it -> sendEvent(it, newComment)
		);

	}
}
