package com.yscorp.withpush.messagesystem.json

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

@Component
class JsonUtil(private val objectMapper: ObjectMapper) {
    fun <T> fromJson(json: String?, clazz: Class<T>?): Optional<T> {
        try {
            return Optional.of(objectMapper.readValue(json, clazz))
        } catch (ex: Exception) {
            log.error("Failed JSON to Object: {}", ex.message)
            return Optional.empty()
        }
    }

    fun toJson(`object`: Any?): Optional<String> {
        try {
            return Optional.of(objectMapper.writeValueAsString(`object`))
        } catch (ex: Exception) {
            log.error("Failed Object to JSON: {}", ex.message)
            return Optional.empty()
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(JsonUtil::class.java)
    }
}
