package com.grizz.wooman.webflux.controller;

import com.grizz.wooman.webflux.controller.dto.ProfileImageResponse;
import com.grizz.wooman.webflux.controller.dto.UserResponse;
import com.grizz.wooman.webflux.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public Mono<UserResponse> getUserById(
            @PathVariable String userId
    ) {
        return ReactiveSecurityContextHolder.getContext()
            .flatMap(context -> {
                final String name = context.getAuthentication()
                    .getName();

                System.out.println("name: " + name);

                return Mono.just(new UserResponse(
                    userId,
                    name,
                    1,
                    1L,
                    null
                ));

            });

        // return ReactiveSecurityContextHolder
        //         .getContext()
        //         .flatMap(context -> {
        //             String name = context.getAuthentication().getName();
        //
        //             if (!name.equals(userId)) {
        //                 return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        //             }
        //
        //             return userService.findById(userId)
        //                     .map(user -> {
        //                         return new UserResponse(
        //                                 user.getId(),
        //                                 user.getName(),
        //                                 user.getAge(),
        //                                 user.getFollowCount(),
        //                                 user.getProfileImage().map(image ->
        //                                         new ProfileImageResponse(
        //                                                 image.getId(),
        //                                                 image.getName(),
        //                                                 image.getUrl()))
        //                         );
        //                     }).switchIfEmpty(
        //                             Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))
        //                     );
        //         });
    }
}
