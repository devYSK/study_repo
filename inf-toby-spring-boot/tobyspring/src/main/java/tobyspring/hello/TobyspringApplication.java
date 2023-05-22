package tobyspring.hello;

import javax.annotation.PostConstruct;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

// @MySpringBootApplication
@SpringBootApplication
public class TobyspringApplication {
	private final JdbcTemplate jdbcTemplate;

	public TobyspringApplication(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@PostConstruct
	void init() {
		jdbcTemplate.execute("create table if not exists hello(name varchar(50) primary key, count int)");
	}

	public static void main(String[] args) {
		SpringApplication.run(TobyspringApplication.class, args);
	}

	@Bean
	ApplicationRunner run(ConditionEvaluationReport report) {
		return args -> System.out.println(report.getConditionAndOutcomesBySource().entrySet().stream()
			.filter(co -> co.getValue().isFullMatch())
			.filter(co -> !co.getKey().contains("Jmx"))
			.peek(co -> {
				System.out.println(co.getKey());
				co.getValue().forEach(c -> {
					System.out.println("\t" + c.getOutcome());
				});
				System.out.println();
			}).count());
	}
}
