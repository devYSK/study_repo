package com.ys.springboot_blog_hodol.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ys.springboot_blog_hodol.domain.Session;

@Repository(value = "iasdfiksafd")
public interface SessionRepository extends CrudRepository<Session, Long> {

    Optional<Session> findByAccessToken(String accessToken);
}
