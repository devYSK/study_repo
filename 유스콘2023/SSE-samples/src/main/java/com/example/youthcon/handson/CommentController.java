package com.example.youthcon.handson;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.youthcon.preparation.Comment;

@RestController
public class CommentController {

	private final CommentService commentService;

	public CommentController(final CommentService commentService) {
		this.commentService = commentService;
	}

	@GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public ResponseEntity<SseEmitter> connect(@RequestParam("articleId") String articleId) {

		SseEmitter emitter = commentService.connect(articleId);
		return ResponseEntity.ok(emitter);
	}

	@PostMapping("/comment")
	public ResponseEntity<Void> sendComment(
		@RequestBody Comment comment,
		@RequestParam("articleId") String articleId) {

		commentService.sendComment(comment, articleId);

		return ResponseEntity.ok().build();
	}

}
