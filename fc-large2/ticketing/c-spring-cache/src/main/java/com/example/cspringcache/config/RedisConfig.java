package com.example.cspringcache.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.example.cspringcache.domain.entity.User;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class RedisConfig {

    @Bean
    RedisTemplate<String, User> uesrRedisTemplate(RedisConnectionFactory connectionFactory) {
        var objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);

        var template = new RedisTemplate<String, User>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(objectMapper, User.class));
        return template;
    }

    @Bean
    RedisTemplate<String, Object> objectRedisTemplate(RedisConnectionFactory connectionFactory) {
        var objectMapper = objectMapper();

        var template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));

        return template;
    }

    private ObjectMapper objectMapper() {
        // PolymorphicTypeValidator를 생성합니다. 이것은 다형성 타입을 처리할 때 안전성을 검증하는 데 사용됩니다.
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator
            .builder()
            .allowIfSubType(Object.class) // Object 클래스의 하위 타입이면 다형성 타입에 대해 검증을 허용합니다.
            .build();

        return new ObjectMapper()
            // 알 수 없는 JSON 속성이 Java 객체에 매핑될 때 예외를 발생시키지 않도록 설정합니다.
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            // Java 8 날짜 및 시간 API를 처리하기 위해 JavaTimeModule을 등록합니다.
            .registerModule(new JavaTimeModule())
            // 기본 타이핑 활성화로, 객체의 실제 구현 타입 정보를 JSON에 포함시킵니다. 비파이널(non-final) 클래스에 대해서만 적용됩니다.
            .activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL)
            // 날짜를 타임스탬프로 직렬화하는 것을 비활성화하여, 날짜를 더 읽기 쉬운 형식으로 직렬화합니다.
            .disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);
    }

}
