package com.example.cspringcache.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cspringcache.domain.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByName(String name);

}
