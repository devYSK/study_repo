package com.ys.jwtbasic.next.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import com.ys.jwtbasic.core.jdbc.ConnectionManager;
import com.ys.jwtbasic.next.model.User;

class UserDaoTest {
	@BeforeAll
	public void setup() {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource("jwp.sql"));
		DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
	}

	@Test
	void crud() throws Exception {
		User expected = new User("userId", "password", "name", "javajigi@email.com");
		UserDao userDao = new UserDao();
		userDao.insert(expected);
		User actual = userDao.findByUserId(expected.getUserId());
		assertEquals(expected, actual);

		expected.update(new User("userId", "password2", "name2", "sanjigi@email.com"));
		userDao.update(expected);
		actual = userDao.findByUserId(expected.getUserId());
		assertEquals(expected, actual);
	}

	@Test
	void findAll() throws Exception {
		UserDao userDao = new UserDao();
		List<User> users = userDao.findAll();
		assertEquals(1, users.size());
	}

}