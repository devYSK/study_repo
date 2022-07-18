package com.ys.springboot_blog_hodol.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.springboot_blog_hodol.domain.Post;
import com.ys.springboot_blog_hodol.repository.PostRepository;
import com.ys.springboot_blog_hodol.request.PostCreate;
import com.ys.springboot_blog_hodol.request.PostEdit;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author : ysk
 */
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청시 h 출력")
    void test() throws Exception {

        // 글 제목
        // 글 내용

        PostCreate postCreate = new PostCreate("제목", "내용");

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreate))
                )
                .andExpect(status().isOk())
                .andExpect(content().string("h"))
                .andDo(print());

    }

    @Test
    @DisplayName("/posts 요청시 Title 필수. ")
    void test2() throws Exception {


        PostCreate postCreate = new PostCreate(null, null);
        // TODO: 2022/07/17 https://ykh6242.tistory.com/entry/MockMvc%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-REST-API%EC%9D%98-Json-Response-%EA%B2%80%EC%A6%9D

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(postCreate))
                )
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청 입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andExpect(status().isBadRequest())

                .andDo(print());

    }

    @Transactional
    @Test
    @DisplayName("/posts 요청시 DB에 저장. ")
    void test3() throws Exception {

        //when
        PostCreate postCreate = new PostCreate("제목입니다.", "내용입니다.");
        // TODO: 2022/07/17 https://ykh6242.tistory.com/entry/MockMvc%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-REST-API%EC%9D%98-Json-Response-%EA%B2%80%EC%A6%9D

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(postCreate))
                )
                .andExpect(status().isOk())
                .andDo(print());
        //then

        assertEquals(1, postRepository.count());
        Post post = postRepository.findAll().get(0);

        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());

    }

    @Transactional
    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        //given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();

        postRepository.save(post);

        //when
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("foo"))
                .andExpect(jsonPath("$.content").value("bar"))

                .andDo(print());
        //then

    }

    @Transactional
    @Test
    @DisplayName("글 여러개 조회")
    void get_post_list() throws Exception {
        //given
        Post post1 = Post.builder()
                .title("1234567891011")
                .content("bar")
                .build();

        postRepository.save(post1);

        Post post2 = Post.builder()
                .title("1234567891011")
                .content("bar")
                .build();

        postRepository.save(post2);


        //when
        mockMvc.perform(get("/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(10)))
                .andExpect(jsonPath("$[0].id").value(post1.getId()))
                .andExpect(jsonPath("$[0].title").value(post1.getTitle()))
                .andExpect(jsonPath("$[0].content").value(post1.getContent()))

                .andDo(print());
        //then

    }

    @Transactional
    @Test
    @DisplayName("글 제목 수정")
    void edit_post_title() throws Exception {
        //given
        Post post = Post.builder()
                .title("응수")
                .content("아크로포레스트")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("영수")
                .build();


        //when
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                )
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()", Matchers.is(10)))
//                .andExpect(jsonPath("$[0].id").value(post1.getId()))
//                .andExpect(jsonPath("$[0].title").value(post1.getTitle()))
//                .andExpect(jsonPath("$[0].content").value(post1.getContent()))

                .andDo(print());
        //then

        Post post1 = postRepository.findById(post.getId()).get();

        assertEquals("영수", post1.getTitle());

    }

    @Transactional
    @Test
    @DisplayName("글 삭제")
    void test8() throws Exception {
        //given
        Post post = Post.builder()
                .title("응수")
                .content("아크로포레스트")
                .build();

        postRepository.save(post);


        //expected
        mockMvc.perform(delete("/posts/{postId}", post.getId())
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(print());
    }
}