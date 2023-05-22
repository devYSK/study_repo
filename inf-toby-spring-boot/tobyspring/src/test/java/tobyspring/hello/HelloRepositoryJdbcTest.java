package tobyspring.hello;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import tobyspring.HelloBootTest;

@HelloBootTest
class HelloRepositoryJdbcTest {

	@Autowired
	private HelloRepository helloRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@BeforeEach
	void init() {
		jdbcTemplate.execute("create table if not exists hello(name varchar(50) primary key, count int)");
	}

	@Test
	void findHelloFailed() {
		assertThat(helloRepository.findHello("Toby")).isNull();
	}

	@Test
	void increaseCount() {

		assertThat(helloRepository.countOf("Ys")).isEqualTo(0);

		helloRepository.increaseCount("Ys");
		assertThat(helloRepository.countOf("Ys")).isEqualTo(1);
		helloRepository.increaseCount("Ys");
		assertThat(helloRepository.countOf("Ys")).isEqualTo(2);

	}

}