package tobyspring;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@HelloBootTest
@Transactional
public class JdbcTemplateTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@BeforeEach
	void init() {
		jdbcTemplate.execute("create table if not exists hello(name varchar(50) primary key, count int)");
	}

	@DisplayName("")
	@Test
	void insertAndQuery1() {
		//given
		jdbcTemplate.update("insert into hello values (?, ?)", "Toby", 3);
		jdbcTemplate.update("insert into hello values (?, ?)", "Spring", 1);

		//when
		Long count = jdbcTemplate.queryForObject("select count(*) from hello", Long.class);


		assertThat(count).isEqualTo(2);

		//then
	}

	@DisplayName("")
	@Test
	void insertAndQuery2() {
		//given
		jdbcTemplate.update("insert into hello values (?, ?)", "Toby", 3);
		jdbcTemplate.update("insert into hello values (?, ?)", "Spring", 1);

		//when
		Long count = jdbcTemplate.queryForObject("select count(*) from hello", Long.class);


		assertThat(count).isEqualTo(2);

		//then
	}
}
