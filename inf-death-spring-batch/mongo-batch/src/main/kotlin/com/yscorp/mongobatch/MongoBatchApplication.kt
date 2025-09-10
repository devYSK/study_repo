package com.yscorp.mongobatch

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component

@SpringBootApplication
class MongoBatchApplication {

//	@Component
//	class MongoBatchApplicationLogger(
//		@Value("\${spring.data.mongodb.uri}") private val uri: String
//	) {
//		private val log = LoggerFactory.getLogger(javaClass)
//
//		@PostConstruct
//		fun printUri() {
//			log.info(">>> Effective Mongo URI = $uri")
//		}
//	}
}

fun main(args: Array<String>) {
	runApplication<MongoBatchApplication>(*args)
}
