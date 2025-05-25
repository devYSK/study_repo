package com.yscorp.withpush.messagesystem.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.security.jackson2.SecurityJackson2Modules
import org.springframework.session.FlushMode
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession

@Configuration
@EnableRedisHttpSession(
    redisNamespace = "message:user_session",
    maxInactiveIntervalInSeconds = 300,
    flushMode = FlushMode.IMMEDIATE
)
@Suppress("unused")
class RedisSessionConfig {
    @Bean
    fun springSessionDefaultRedisSerializer(): RedisSerializer<Any> {
        val mapper = ObjectMapper()
        mapper.registerModules(SecurityJackson2Modules.getModules(javaClass.classLoader))
        return GenericJackson2JsonRedisSerializer(mapper)
    }
}
