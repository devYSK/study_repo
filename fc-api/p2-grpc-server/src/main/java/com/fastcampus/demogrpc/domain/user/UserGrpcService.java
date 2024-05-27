package com.fastcampus.demogrpc.domain.user;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;

import net.devh.boot.grpc.server.service.GrpcService;

import com.fastcampus.demogrpc.global.interceptor.AccessLoggingInterceptor;
import com.fastcampus.demogrpc.global.interceptor.BasicAuthInterceptor;

import io.grpc.stub.StreamObserver;
import user.UserOuterClass;
import user.UserServiceGrpc;

@GrpcService(interceptors = {AccessLoggingInterceptor.class, BasicAuthInterceptor.class})
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {

	private static final Map<String, User> userMap = new ConcurrentHashMap<>();

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserGrpcService.class);

	static {
		for (int i = 1; i <= 10; i++) {
			User user = new User(String.valueOf(i), "user" + i, 20 + i);
			userMap.put(user.getId(), user);
		}
	}

	@Override
	public void getUser(
		final UserOuterClass.GetUserRequest request,
		final StreamObserver<UserOuterClass.User> responseObserver
	) {

		final String username = request.getUsername();

		final User user = userMap.entrySet()
			.stream()
			.filter(it -> it.getValue()
				.getUsername()
				.equals(username))
			.map(Map.Entry::getValue)
			.findFirst()
			.orElse(null);

		if (user == null) {
			responseObserver.onError(new RuntimeException("User not found"));
			return;
		}

		responseObserver.onNext(UserOuterClass.User.newBuilder()
			.setId(user.getId())
			.setName(user.getUsername())
			.setAge(user.getAge())
			.build());

		responseObserver.onCompleted();
	}

	@Override
	public void getAllUsers(final UserOuterClass.GetAllUsersRequest request,
		final StreamObserver<UserOuterClass.User> responseObserver) {

		String requestedUsername = request.getUsername().isEmpty() ? null : request.getUsername();

		Long requestedAge = request.getAge() == 0 ? null : request.getAge();

		log.info("requestedUsername: {}, requestedAge: {}", requestedUsername, requestedAge);

		userMap.values()
			.stream()
			.filter(user -> (requestedUsername == null || user.getUsername().equals(requestedUsername)) &&
				(requestedAge == null || user.getAge() == requestedAge))
			.map(user -> UserOuterClass.User.newBuilder()
				.setId(user.getId())
				.setName(user.getUsername())
				.setAge(user.getAge())
				.build())
			.forEach(responseObserver::onNext);

		responseObserver.onCompleted();
	}

}
