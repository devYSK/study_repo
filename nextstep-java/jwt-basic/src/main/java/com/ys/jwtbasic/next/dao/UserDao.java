package com.ys.jwtbasic.next.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ys.jwtbasic.core.jdbc.ConnectionManager;
import com.ys.jwtbasic.core.jdbc.JdbcTemplate;
import com.ys.jwtbasic.core.jdbc.RowMapper;
import com.ys.jwtbasic.next.model.User;

public class UserDao {
	public void insert(User user) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
		jdbcTemplate.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
	}

	public User findByUserId(String userId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

		RowMapper<User> rm = rs -> new User(rs.getString("userId"),
			rs.getString("password"),
			rs.getString("name"),
			rs.getString("email"));

		return jdbcTemplate.queryForObject(sql, rm, userId);
	}

	public List<User> findAll() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		String sql = "SELECT userId, password, name, email FROM USERS";

		RowMapper<User> rm = rs -> new User(rs.getString("userId"),
			rs.getString("password"),
			rs.getString("name"),
			rs.getString("email"));

		return jdbcTemplate.query(sql, rm);
	}

	public void update(User user) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		String sql = "UPDATE USERS set password = ?, name = ?, email = ? WHERE userId = ?";
		jdbcTemplate.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
	}
}