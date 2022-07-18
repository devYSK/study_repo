package com.ys.springboot_blog_hodol.domain;

import lombok.Builder;
import lombok.Getter;

/**
 * @author : ysk
 */
@Getter
public class PostEditor {

    private String title;

    private String content;

    @Builder
    public PostEditor(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
