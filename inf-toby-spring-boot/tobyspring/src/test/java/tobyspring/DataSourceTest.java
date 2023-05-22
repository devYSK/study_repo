package tobyspring;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.transaction.annotation.Transactional;


@JdbcTest
@Transactional
class DataSourceTest {
	@Autowired
	DataSource dataSource;

	@DisplayName("")
	@Test
	void connect() throws SQLException {
		//given
		Connection connection = dataSource.getConnection();
		connection
			.close();
		//when

		//then
	}
}
