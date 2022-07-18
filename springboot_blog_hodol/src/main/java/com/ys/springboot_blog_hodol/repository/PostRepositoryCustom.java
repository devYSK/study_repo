package com.ys.springboot_blog_hodol.repository;

import com.ys.springboot_blog_hodol.domain.Post;
import com.ys.springboot_blog_hodol.request.PostSearch;

import java.util.List;

/**
 * @author : ysk
 */
public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);

}
