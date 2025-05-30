package hello.datasource;

import java.time.Duration;
import java.util.List;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
public class MyDataSource {
	private String url;
	private String username;
	private String password;
	private int maxConnection;
	private Duration timeout;
	private List<String> options;
	//
	// public MyDataSource(String url, String username, String password, int
	// 	maxConnection, Duration timeout, List<String> options) {
	// 	this.url = url;
	// 	this.username = username;
	// 	this.password = password;
	// 	this.maxConnection = maxConnection;
	// 	this.timeout = timeout;
	// 	this.options = options;
	// }

	@PostConstruct
	public void init() {
		log.info("url={}", url);
		log.info("username={}", username);
		log.info("password={}", password);
		log.info("maxConnection={}", maxConnection);
		log.info("timeout={}", timeout);
		log.info("options={}", options);
	}
}