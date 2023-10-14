package com.ys.webfluxexample1.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.ys.webfluxexample1.common.EmptyImage;
import com.ys.webfluxexample1.common.Image;
import com.ys.webfluxexample1.common.User;
import com.ys.webfluxexample1.repository.UserReactorRepository;

import reactor.core.publisher.Mono;

@Service
public class UserService {
	private WebClient webClient = WebClient.create("http://localhost:8081");

	private final UserReactorRepository userRepository = new UserReactorRepository();

	public Mono<User> findById(String userId) {
		return userRepository.findById(userId)
			.flatMap(userEntity -> {
				String imageId = userEntity.getProfileImageId();

				Map<String, String> uriVariableMap = Map.of("imageId", imageId);

				return webClient.get()
					.uri("/api/images/{imageId}", uriVariableMap)
					.retrieve()
					.toEntity(ImageResponse.class)
					.map(HttpEntity::getBody)
					.map(imageResp -> new Image(
						imageResp.getId(),
						imageResp.getName(),
						imageResp.getUrl()
					))
					.switchIfEmpty(Mono.just(new EmptyImage()))
					.map(image -> {
						Optional<Image> profileImage = Optional.empty();

						if (!(image instanceof EmptyImage)) {
							profileImage = Optional.of(image);
						}

						return new User(
							userEntity.getId(),
							userEntity.getName(),
							userEntity.getAge(),
							profileImage,
							List.of(),
							0L
						);
					});
			});
	}
}
