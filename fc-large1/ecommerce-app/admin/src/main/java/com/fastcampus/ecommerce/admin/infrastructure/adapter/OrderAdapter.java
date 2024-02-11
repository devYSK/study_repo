package com.fastcampus.ecommerce.admin.infrastructure.adapter;

import com.fastcampus.ecommerce.admin.service.dto.OrderDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderAdapter {

    private final RestTemplate restTemplate;

    @Value("${apis.order-api.url}")
    private String orderApiUrl;

    public List<OrderDTO> findAll(Pageable pageable) {
        log.info(">>> Order API 요청 URL: {}", orderApiUrl);

        Map uriVariables = Map.of("page", pageable.getPageNumber(), "size", pageable.getPageSize());
        OrderDTO[] orderDTOS = restTemplate.getForObject(orderApiUrl, OrderDTO[].class, uriVariables);

        log.info("<<< Order API 응답: {}", orderDTOS);
        return Arrays.asList(orderDTOS);
    }
}
