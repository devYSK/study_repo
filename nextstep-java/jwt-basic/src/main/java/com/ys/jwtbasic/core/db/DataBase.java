package com.ys.jwtbasic.core.db;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.ys.jwtbasic.next.model.User;

public class DataBase {
	private static Map<String, User> users = new HashMap<>();

	static {
		users.put("admin", new User("admin", "password", "자바지기", "admin@slipp.net"));
	}

	public static void addUser(User user) {
		users.put(user.getUserId(), user);
	}

	public static User findUserById(String userId) {
		return users.get(userId);
	}

	public static Collection<User> findAll() {
		return users.values();
	}

}