package com.ys.springboot_blog_hodol.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.ys.springboot_blog_hodol.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmailAndPassword(String email, String password);

    Optional<User> findByEmail(String email);
}
