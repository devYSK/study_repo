package hello.datasource;

import java.time.Duration;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import lombok.Getter;

@Getter
@ConfigurationProperties("my.datasource")
public class MyDataSourcePropertiesV2 {
	private String url;
	private String username;
	private String password;
	private Etc etc;

	public MyDataSourcePropertiesV2(String url, String username, String
		password, @DefaultValue Etc etc // DefaultValue 해당 값을 찾을 수 없는 경우 기본값을 사용한다.
	) {
		this.url = url;
		this.username = username;
		this.password = password;
		this.etc = etc;
	}

	@Getter
	public static class Etc {
		private int maxConnection;
		private Duration timeout;
		private List<String> options;

		public Etc(int maxConnection, Duration timeout,
			@DefaultValue("DEFAULT") List<String> options) {
			this.maxConnection = maxConnection;
			this.timeout = timeout;
			this.options = options;
		}
	}
}