package com.example.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrderEventStreamListener implements StreamListener<String, MapRecord<String, String, String>> {

    int paymentProcessId = 0;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        Map map = message.getValue();

        String userId = (String) map.get("userId");
        String productId = (String) map.get("productId");
        String price = (String) map.get("price");

        // 결제 관련 로직 처리
        // ...

        String paymentIdStr = Integer.toString(paymentProcessId++);

        // 결제 완료 이벤트 발행
        Map fieldMap = new HashMap<String, String>();
        fieldMap.put("userId", userId);
        fieldMap.put("productId", productId);
        fieldMap.put("price", price);
        fieldMap.put("paymentProcessId", paymentIdStr);

        redisTemplate.opsForStream().add("payment-events", fieldMap);

        System.out.println("[Order consumed] Created payment: " + paymentIdStr);
    }
}
