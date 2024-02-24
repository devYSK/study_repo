package com.example.cachelayer.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ExternalApiService {

	public String getUserName(String userId) {
		// 외부 서비스나 DB 호출
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}

		System.out.println("Getting user name from other service..");

		if (userId.equals("A")) {
			return "Adam";
		}
		if (userId.equals("B")) {
			return "Bob";
		}

		return "";
	}

	@Cacheable(cacheNames = "userAgeCache", key = "#userId")
	public int getUserAge(String userId) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}

		System.out.println("Getting user age from other service..");

		if (userId.equals("A")) {
			return 28;
		}
		if (userId.equals("B")) {
			return 32;
		}

		return 0;
	}

}
