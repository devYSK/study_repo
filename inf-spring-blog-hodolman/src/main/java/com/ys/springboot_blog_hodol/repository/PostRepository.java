package com.ys.springboot_blog_hodol.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ys.springboot_blog_hodol.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

}
