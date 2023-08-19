package com.ys.springboot_blog_hodol.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ys.springboot_blog_hodol.domain.Post;
import com.ys.springboot_blog_hodol.exception.PostNotFound;
import com.ys.springboot_blog_hodol.repository.PostRepository;
import com.ys.springboot_blog_hodol.request.PostCreate;
import com.ys.springboot_blog_hodol.request.PostEdit;
import com.ys.springboot_blog_hodol.response.PostResponse;

/**
 * @author : ysk
 */
@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Transactional
    @Test
    @DisplayName("글 작성")
    void write_test() {
        //given

        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        //when
        postService.write(postCreate);

        //then

        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());

    }

    @Transactional
    @Test
    @DisplayName("글 1개 조회")
    void get_one_test() {
        //given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);
        Long postId = post.getId();
        //when

        PostResponse findPost = postService.get(postId);

        //then
        assertNotNull(findPost);
        assertEquals(findPost.getTitle(), "foo");
        assertEquals(findPost.getContent(), "bar");
    }

    @Transactional
    @Test
    @DisplayName("글 여러개 조회")
    void get_post_list() throws Exception {
        //given


        List<Post> requestPosts = IntStream.range(0, 30)
                .mapToObj(i -> Post.builder().title("제목 " + i)
                        .content("내용 " + i)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // PostSearch postSearch = new PostSearch(1, 10);
        //when
        // List<PostResponse> posts = postService.getList(postSearch);
        //then

        // assertEquals(10, posts.size());
    }


    @Transactional
    @Test
    @DisplayName("글 제목 수정")
    void edit_post_title() {
        Post post = Post.builder()
                .title("응수")
                .content("아크로포레스트")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("영수")
                .build();

        postService.edit(post.getId(), postEdit);

        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));

        assertEquals("영수", changedPost.getTitle());

    }

    @Transactional
    @Test
    @DisplayName("글 내용 수정")
    void edit_post_content() {
        Post post = Post.builder()
                .title("응수")
                .content("아크로포레스트")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .content("반포자이")
                .build();

        postService.edit(post.getId(), postEdit);

        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));

        assertEquals("반포자이", changedPost.getContent());

    }
    @Transactional
    @Test
    @DisplayName("글 제목 수정")
    void edit_post_title_is_not_null() {
        Post post = Post.builder()
                .title("응수")
                .content("아크로포레스트")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("영수")
                .content("아크로포레스트")
                .build();

        postService.edit(post.getId(), postEdit);

        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));

        assertEquals("영수", changedPost.getTitle());
        assertEquals("아크로포레스트", changedPost.getContent());
    }


    @Transactional
    @Test
    @DisplayName("글 내용 삭제")
    void delete_post() {
        Post post = Post.builder()
                .title("영수")
                .content("아크로포레스트")
                .build();

        postRepository.save(post);

        //when
        postService.delete(post.getId());

        //then

        assertEquals(0, postRepository.count());
    }

    @Transactional
    @Test
    @DisplayName("글 1개 조회 시 없으면 예외 던짐")
    void get_one_throw_exception() {
        //given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);
        //expected
        assertThrows(PostNotFound.class, () -> postService.get(post.getId() + 999L));

    }
}