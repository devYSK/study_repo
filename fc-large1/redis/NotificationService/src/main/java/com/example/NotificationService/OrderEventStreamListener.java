package com.example.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrderEventStreamListener implements StreamListener<String, MapRecord<String, String, String>> {

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        Map map = message.getValue();

        String userId = (String) map.get("userId");
        String productId = (String) map.get("productId");

        // 주문 건에 대한 메일 발송 처리
        System.out.println("[Order consumed] usrId: " + userId + "   productId: " + productId);
    }
}
