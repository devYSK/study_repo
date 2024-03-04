package com.ys.userservice.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ys.userservice.Greeting;
import com.ys.userservice.dto.RequestUser;
import com.ys.userservice.dto.ResponseUser;
import com.ys.userservice.dto.UserDto;
import com.ys.userservice.jpa.UserEntity;
import com.ys.userservice.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

	private final Environment env;

	private final Greeting greeting;

	private final UserService userService;

	@GetMapping("/health_check")
	// @Timed(value="users.status", longTask = true)
	public ServiceInfo status() {
		return new ServiceInfo(
			env.getProperty("local.server.port"),
			env.getProperty("server.port"),
			env.getProperty("gateway.ip"),
			greeting.getIp(),
			env.getProperty("greeting.message"),
			greeting.getSecret(),
			env.getProperty("token.expiration_time"),
			env.getProperty("test.sexy"),
			env.getProperty("test.db"),
			"null"
		);
	}

	@PostMapping("/users")
	public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		UserDto userDto = mapper.map(user, UserDto.class);
		userService.createUser(userDto);

		ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);

		return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
	}

	@GetMapping("/users")
	public ResponseEntity<List<ResponseUser>> getUsers() {
		Iterable<UserEntity> userList = userService.getUserByAll();

		List<ResponseUser> result = new ArrayList<>();
		userList.forEach(v -> {
			result.add(new ModelMapper().map(v, ResponseUser.class));
		});

		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	@GetMapping("/users/{userId}")
	public ResponseEntity getUser(@PathVariable("userId") String userId) {
		UserDto userDto = userService.getUserByUserId(userId);

		ResponseUser returnValue = new ModelMapper().map(userDto, ResponseUser.class);

		EntityModel entityModel = EntityModel.of(returnValue);
		WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).getUsers());
		entityModel.add(linkTo.withRel("all-users"));

		try {
			return ResponseEntity.status(HttpStatus.OK).body(entityModel);
		} catch (Exception ex) {
			throw new RuntimeException();
		}
	}

	@GetMapping("/users/hateoas")
	public ResponseEntity<CollectionModel<EntityModel<ResponseUser>>> getUsersWithHateoas() {
		List<EntityModel<ResponseUser>> result = new ArrayList<>();
		Iterable<UserEntity> users = userService.getUserByAll();

		for (UserEntity user : users) {
			EntityModel entityModel = EntityModel.of(user);
			entityModel.add(linkTo(methodOn(this.getClass()).getUser(user.getUserId())).withSelfRel());

			result.add(entityModel);
		}

		return ResponseEntity.ok(CollectionModel.of(result, linkTo(methodOn(this.getClass()).getUsersWithHateoas()).withSelfRel()));
	}

	public record ServiceInfo(
		String localServerPort,
		String serverPort,
		String gatewayIpEnv,
		String gatewayIpValue,
		String message,
		String tokenSecret,
		String tokenExpirationTime,
		String message1,
		String message2,
		String message3
	) {}
}
