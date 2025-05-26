package com.yscorp.withpush.messagesystem.json

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

@Component
class JsonUtil(private val objectMapper: ObjectMapper) {

    /**
     * JSON 문자열을 지정된 클래스 타입의 객체로 역직렬화합니다.
     * 역직렬화에 실패하면 null을 반환합니다.
     */
    fun <T> fromJson(json: String?, clazz: Class<T>): T? {
        return runCatching {
            objectMapper.readValue(json, clazz)
        }.getOrElse {
            log.error("Failed JSON to Object: {}", it.message)
            null
        }
    }

    /**
     * 객체를 JSON 문자열로 직렬화합니다.
     * 직렬화에 실패하면 null을 반환합니다.
     */
    fun toJson(obj: Any?): String? {
        return runCatching {
            objectMapper.writeValueAsString(obj)
        }.getOrElse {
            log.error("Failed Object to JSON: {}", it.message)
            null
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(JsonUtil::class.java)
    }
}
