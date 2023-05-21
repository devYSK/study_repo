package tobyspring;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

class HelloApiTest {

	@Test
	void helloApi() {
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<String> res = restTemplate.getForEntity(
			"http://localhost:8080/hello?name={name}", String.class, "Spring");
		assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(res.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE)
			.startsWith(MediaType.TEXT_PLAIN_VALUE)).isTrue();
		assertThat(res.getBody().trim()).isEqualTo("Hello Spring");
	}

}