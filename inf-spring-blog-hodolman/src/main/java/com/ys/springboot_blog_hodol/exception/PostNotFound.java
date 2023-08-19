package com.ys.springboot_blog_hodol.exception;

/**
 * @author : ysk
 */
public class PostNotFound extends RuntimeException{

    private static final String MESSAGE = "존재하지 않는 글입니다.";

    public PostNotFound() {
        super(MESSAGE);
    }

}
