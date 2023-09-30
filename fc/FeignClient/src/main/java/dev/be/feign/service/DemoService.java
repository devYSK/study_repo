package dev.be.feign.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import dev.be.feign.common.dto.BaseRequestInfo;
import dev.be.feign.common.dto.BaseResponseInfo;
import dev.be.feign.feign.client.DemoFeignClient;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DemoService {

    private final DemoFeignClient client;

    public String get() {
        ResponseEntity<BaseResponseInfo> response = client.callGet("CustomHeader",
                                                                   "CustomName",
                                                                   1L);
        System.out.println("Name : " + response.getBody().getName());
        System.out.println("Age : " + response.getBody().getAge());
        System.out.println("Header : " + response.getBody().getHeader());
        return "get";
    }

    public String post() {
        BaseRequestInfo requestBody = BaseRequestInfo.builder()
                                                     .name("customName")
                                                     .age(1L)
                                                     .build();
        ResponseEntity<BaseResponseInfo> response = client.callPost("CustomHeader",
                                                                    requestBody);
        System.out.println("Name : " + response.getBody().getName());
        System.out.println("Age : " + response.getBody().getAge());
        System.out.println("Header : " + response.getBody().getHeader());
        return "post";
    }

    public String errorDecoder() {
        client.callErrorDecoder();
        return "errorDecoder";
    }
}