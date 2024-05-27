package com.fastcampus.demogrpcclient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.devh.boot.grpc.client.inject.GrpcClient;

import lombok.RequiredArgsConstructor;
import user.UserOuterClass;
import user.UserServiceGrpc;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserGrpcClient userGrpcClient;

	@GetMapping
	public ResponseEntity<String> getUserList(@RequestParam(required = false) String username, @RequestParam(required = false) Integer age) {
		return ResponseEntity.ok().body(JsonConverter.toJsonList(this.userGrpcClient.findAll(username, age)));
	}

	@GetMapping("/{username}")
	public ResponseEntity<String> getUserById(@PathVariable String username) {
		return ResponseEntity.ok().body(JsonConverter.toJson(this.userGrpcClient.findById(username)));
	}

	@Service
	public static class UserGrpcClient {
		@GrpcClient("local-grpc-server")
		private UserServiceGrpc.UserServiceBlockingStub userServiceStub;

		public UserOuterClass.User findById(final String username) {
			// Optional로 nullable 필드 처리
			String usernameOrEmpty = Optional.ofNullable(username).orElse("");
			return this.userServiceStub.getUser(
				UserOuterClass.GetUserRequest
					.newBuilder()
					.setUsername(usernameOrEmpty)
					.build()
			);
		}

		public List<UserOuterClass.User> findAll(String username, Integer age) {
			UserOuterClass.GetAllUsersRequest.Builder requestBuilder = UserOuterClass.GetAllUsersRequest.newBuilder();

			// Optional로 nullable 필드 처리
			Optional.ofNullable(username).ifPresent(requestBuilder::setUsername);
			Optional.ofNullable(age).ifPresent(requestBuilder::setAge);

			Iterator<UserOuterClass.User> userIterator = this.userServiceStub.getAllUsers(requestBuilder.build());

			List<UserOuterClass.User> userList = new ArrayList<>();
			while (userIterator.hasNext()) {
				userList.add(userIterator.next());
			}

			return userList;
		}
	}

}
