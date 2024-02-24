package com.example.cachelayer.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.example.cachelayer.dto.UserProfile;

@Service
public class UserService {

	@Autowired
	private ExternalApiService externalApiService;

	@Autowired
	StringRedisTemplate redisTemplate;

	public UserProfile getUserProfile(String userId) {

		String userName = null;

		ValueOperations<String, String> ops = redisTemplate.opsForValue();
		String cachedName = ops.get("nameKey:" + userId);

		if (cachedName != null) {
			userName = cachedName;
		} else {
			userName = externalApiService.getUserName(userId);
			ops.set("nameKey:" + userId, userName, 5, TimeUnit.SECONDS);
		}

		//String userName = externalApiService.getUserName(userId);
		int userAge = externalApiService.getUserAge(userId);

		return new UserProfile(userName, userAge);
	}

}
