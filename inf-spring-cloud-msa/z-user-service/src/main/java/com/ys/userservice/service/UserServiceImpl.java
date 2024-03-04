package com.ys.userservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ys.userservice.client.CatalogServiceClient;
import com.ys.userservice.client.OrderServiceClient;
import com.ys.userservice.dto.ResponseCatalog;
import com.ys.userservice.dto.ResponseOrder;
import com.ys.userservice.dto.UserDto;
import com.ys.userservice.jpa.UserEntity;
import com.ys.userservice.jpa.UserRepository;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final Environment env;
	private final OrderServiceClient orderServiceClient;
	private final CatalogServiceClient catalogServiceClient;

	private final CircuitBreakerFactory circuitBreakerFactory;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(username);

		if (userEntity == null) {
			throw new UsernameNotFoundException(username + ": not found");
		}

		return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
			true, true, true, true,
			new ArrayList<>());
	}

	@Override
	public UserDto createUser(UserDto userDto) {
		userDto.setUserId(UUID.randomUUID()
			.toString());

		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration()
			.setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity userEntity = mapper.map(userDto, UserEntity.class);
		userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));

		userRepository.save(userEntity);

		UserDto returnUserDto = mapper.map(userEntity, UserDto.class);

		return returnUserDto;
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null) {
			throw new UsernameNotFoundException("User not found");
		}

		UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

		log.info("Before call orders microservice");

		CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker1");
		// CircuitBreaker circuitBreaker2 = circuitBreakerFactory.create("circuitBreaker2");

		List<ResponseOrder> ordersList = circuitBreaker.run(() -> orderServiceClient.getOrders(userId),
			throwable -> new ArrayList<>());

		log.info("After called orders microservice");

		// List<ResponseCatalog> catalogList = catalogServiceClient.getCatalogs();

		userDto.setOrders(ordersList);
		// userDto.setCatalogs(catalogList);


		return userDto;
	}

	@Override
	public Iterable<UserEntity> getUserByAll() {
		return userRepository.findAll();
	}

	@Override
	public UserDto getUserDetailsByEmail(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null) {
			throw new UsernameNotFoundException(email);
		}

		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration()
			.setMatchingStrategy(MatchingStrategies.STRICT);

		UserDto userDto = mapper.map(userEntity, UserDto.class);
		return userDto;
	}
}
