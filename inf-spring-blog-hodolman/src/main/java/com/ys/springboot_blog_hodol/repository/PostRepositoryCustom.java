package com.ys.springboot_blog_hodol.repository;

import java.util.List;

import com.ys.springboot_blog_hodol.domain.Post;
import com.ys.springboot_blog_hodol.request.PostSearch;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
