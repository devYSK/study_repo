package com.ys.springboot_blog_hodol.repository;

import java.util.List;

import com.ys.springboot_blog_hodol.domain.Post;
import com.ys.springboot_blog_hodol.request.PostSearch;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    @Override
    public List<Post> getList(PostSearch postSearch) {
        // return jpaQueryFactory.selectFrom(post)
        //         .limit(postSearch.getSize())
        //         .offset(postSearch.getOffset())
        //         .orderBy(post.id.desc())
        //         .fetch();
        return null;
    }
}
