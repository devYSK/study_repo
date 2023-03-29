package com.ys.jwtbasic.next.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.ys.jwtbasic.core.jdbc.JdbcTemplate;
import com.ys.jwtbasic.core.jdbc.RowMapper;
import com.ys.jwtbasic.next.model.Question;

public class QuestionDao {
	public List<Question> findAll() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		String sql = "SELECT questionId, writer, title, createdDate, countOfAnswer FROM QUESTIONS "
			+ "order by questionId desc";

		RowMapper<Question> rm = rs -> new Question(rs.getLong("questionId"), rs.getString("writer"), rs.getString("title"), null,
			rs.getTimestamp("createdDate"), rs.getInt("countOfAnswer"));

		return jdbcTemplate.query(sql, rm);
	}

	public Question findById(long questionId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		String sql = "SELECT questionId, writer, title, contents, createdDate, countOfAnswer FROM QUESTIONS "
			+ "WHERE questionId = ?";

		RowMapper<Question> rm = rs -> new Question(rs.getLong("questionId"), rs.getString("writer"), rs.getString("title"),
			rs.getString("contents"), rs.getTimestamp("createdDate"), rs.getInt("countOfAnswer"));

		return jdbcTemplate.queryForObject(sql, rm, questionId);
	}
}