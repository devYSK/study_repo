package com.ys.test;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.redis.testcontainers.RedisContainer;

// @Ignore
@SpringBootTest
@ContextConfiguration(initializers = IntegrationTest.IntegrationTestInitializer.class)
@Testcontainers
public class IntegrationTest {

	@Container
	static DockerComposeContainer rdbms;

	static RedisContainer redis;

	static LocalStackContainer aws;

	static KafkaContainer kafka;

	static {
		// 루트 디렉토리
		rdbms = new DockerComposeContainer(new File("infra/test/docker-compose.yml"))
			.withExposedService(
				"local-db", // 컴포즈내의 서비스명
				3306, // 서비스 포트
				Wait.forLogMessage(".*ready for connections.*", 1)
					.withStartupTimeout(Duration.ofSeconds(300))
			)
			.withExposedService(
				"local-db-migrate", // 컴포즈 내의 서비스명
				0, // 서비스 포트. 없으면 0
				Wait.forLogMessage("(.*Successfully applied.*)|(.*Successfully validated.*)", 1)
					.withStartupTimeout(Duration.ofSeconds(300))
			)
		// .withLocalCompose(true)
		;

		rdbms.start();

		redis = new RedisContainer(RedisContainer.DEFAULT_IMAGE_NAME.withTag("6"));

		// 생략 ...
		redis.start();

		DockerImageName imageName = DockerImageName.parse("localstack/localstack:latest");
		aws = (new LocalStackContainer(imageName))
			.withServices(LocalStackContainer.Service.S3)
			.withStartupTimeout(Duration.ofSeconds(600));
		aws.start();

		kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"))
			.withKraft();

		kafka.start();
	}

	static class IntegrationTestInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		@Override
		public void initialize(ConfigurableApplicationContext applicationContext) {
			Map<String, String> properties = new HashMap<>();

			var rdbmsHost = rdbms.getServiceHost("local-db", 3306);
			var rdbmsPort = rdbms.getServicePort("local-db", 3306);

			properties.put("spring.datasource.url", "jdbc:mysql://" + rdbmsHost + ":" + rdbmsPort + "/score");

			var redisHost = redis.getHost();
			var redisPort = redis.getFirstMappedPort();

			properties.put("spring.data.redis.host", redisHost);
			properties.put("spring.data.redis.port", redisPort.toString());

			try {
				aws.execInContainer(
					"awslocal",
					"s3api",
					"create-bucket",
					"--bucket",
					"test-bucket"
				);

				System.out.println("endPoint!! " + aws.getEndpoint().toString());
				properties.put("aws.endpoint", aws.getEndpoint().toString());
			} catch (Exception e) {
				// ignore ..
			}

			properties.put("spring.kafka.bootstrap-servers", kafka.getBootstrapServers());

			TestPropertyValues.of(properties)
							  .applyTo(applicationContext); // 마지막에 설정 해야 적용된다
		}
	}
}
