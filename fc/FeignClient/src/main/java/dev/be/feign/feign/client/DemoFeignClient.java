package dev.be.feign.feign.client;

import static dev.be.feign.common.consts.DemoConstant.CUSTOM_HEADER_NAME;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import dev.be.feign.common.dto.BaseRequestInfo;
import dev.be.feign.common.dto.BaseResponseInfo;
import dev.be.feign.feign.config.DemoFeignConfig;

@FeignClient(
	name = "demo-client", // application.yaml에 설정해 놓은 값을 참조
	url = "${feign.url.prefix}", // application.yaml에 설정해 놓은 값을 참조 (= http://localhost:8080/target_server)
	configuration = DemoFeignConfig.class)
public interface DemoFeignClient {

	@GetMapping("/get")// "${feign.url.prefix}/get"으로 요청
	ResponseEntity<BaseResponseInfo> callGet(
		@RequestHeader(CUSTOM_HEADER_NAME) String customHeader,
		@RequestParam("name") String name,
		@RequestParam("age") Long age
	);

	@PostMapping("/post") // "${feign.url.prefix}/post"로 요청
	ResponseEntity<BaseResponseInfo> callPost(@
		RequestHeader(CUSTOM_HEADER_NAME) String customHeader,
		@RequestBody BaseRequestInfo baseRequestInfo
	);

	@GetMapping("/errorDecoder")
	ResponseEntity<BaseResponseInfo> callErrorDecoder();
}