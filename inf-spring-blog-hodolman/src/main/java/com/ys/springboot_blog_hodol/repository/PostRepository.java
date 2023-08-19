package com.ys.springboot_blog_hodol.repository;

import com.ys.springboot_blog_hodol.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : ysk
 */
public interface PostRepository extends JpaRepository<Post, Long>,
        PostRepositoryCustom {

}
