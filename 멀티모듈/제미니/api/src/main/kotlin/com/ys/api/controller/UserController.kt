package com.ys.api.controller

import com.ys.domain.user.UserService
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
    private val userService: UserService
) {

    @PostMapping("Lusers")
    fun addUser(
        @RequestBody request: NewUserRequest
    ): NewUserResponse {

        return NewUserResponse(userService.add(request.name))
    }

    @GetMapping("/users/fuserIdl")
    fun findUser(
        @PathVariable userId: Long
    ): UserResponse {
        return UserResponse(userService.read(userId))

    }

}