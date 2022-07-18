package com.ys.springboot_blog_hodol.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ys.springboot_blog_hodol.domain.Post;
import com.ys.springboot_blog_hodol.domain.QPost;
import com.ys.springboot_blog_hodol.request.PostSearch;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ys.springboot_blog_hodol.domain.QPost.post;

/**
 * @author : ysk
 */
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(PostSearch postSearch) {
        return jpaQueryFactory.selectFrom(post)
                .limit(postSearch.getSize())
                .offset(postSearch.getOffSet())
                .orderBy(post.id.desc())
                .fetch();
    }
}
