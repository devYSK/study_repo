package com.yscorp.withpush.messagesystem.controller

import com.yscorp.withpush.messagesystem.dto.restapi.UserRegisterRequest
import com.yscorp.withpush.messagesystem.service.UserService
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
@Suppress("unused")
class UserController(private val userService: UserService) {

    @PostMapping("/register")
    fun register(@RequestBody request: UserRegisterRequest): ResponseEntity<String> {
        try {
            userService.addUser(request.username, request.password)
            return ResponseEntity.ok<String>("User registered.")
        } catch (ex: Exception) {
            log.error("Register user failed. cause: {}", ex.message)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body<String>("Register user failed.")
        }
    }

    @PostMapping("/unregister")
    fun unregister(request: HttpServletRequest): ResponseEntity<String> {
        try {
            userService.removeUser()
            request.session.invalidate()
            return ResponseEntity.ok("User unregistered.")
        } catch (ex: Exception) {
            log.error("Unregister user failed. cause: {}", ex.message)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body<String>("Unregister user failed.")
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(UserController::class.java)
    }
}
