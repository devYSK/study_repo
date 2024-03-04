package com.ys.userservice.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ys.userservice.config.FeignConfig;
import com.ys.userservice.dto.ResponseOrder;

@FeignClient(name="order-service", configuration = FeignConfig.OrderFeignErrorDecoder.class)
public interface OrderServiceClient {

    @GetMapping("/order-service/{userId}/orders")
    List<ResponseOrder> getOrders(@PathVariable String userId);

}
