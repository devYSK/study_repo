package com.fastcampus.demogrpc.domain.user;

import lombok.Getter;

@Getter
public class User {
	private String  id;
	private String username;
	private int age;

	public User(String id, String username, int age) {
		this.id = id;
		this.username = username;
		this.age = age;
	}

	@Override
	public String toString() {
		return "User{id=" + id + ", username='" + username + "', age=" + age + '}';
	}
}