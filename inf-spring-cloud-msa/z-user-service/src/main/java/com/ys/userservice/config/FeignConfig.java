package com.ys.userservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import feign.Logger;
import feign.Response;
import feign.codec.ErrorDecoder;

@Configuration
public class FeignConfig {

	@Bean
	public Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}

	@Component
	public static class CatalogFeignErrorDecoder implements ErrorDecoder {
		Environment env;

		@Autowired
		public CatalogFeignErrorDecoder(Environment env) {
			this.env = env;
		}

		@Override
		public Exception decode(String methodKey, Response response) {
			switch(response.status()) {
				case 400:
					break;
				case 404:
					if (methodKey.contains("getCatalogs")) {
						return new ResponseStatusException(HttpStatus.valueOf(response.status()),
							"Not catalogs");
					}
					break;
				default:
					return new Exception(response.reason());
			}

			return null;
		}
	}

	@Component
	public static class OrderFeignErrorDecoder implements ErrorDecoder {
		Environment env;

		@Autowired
		public OrderFeignErrorDecoder(Environment env) {
			this.env = env;
		}

		@Override
		public Exception decode(String methodKey, Response response) {
			switch(response.status()) {
				case 400:
					break;
				case 404:
					if (methodKey.contains("getOrders")) {
						return new ResponseStatusException(HttpStatus.valueOf(response.status()),
							"User's orders is empty");
					}
					break;
				default:
					return new Exception(response.reason());
			}

			return null;
		}
	}


}
