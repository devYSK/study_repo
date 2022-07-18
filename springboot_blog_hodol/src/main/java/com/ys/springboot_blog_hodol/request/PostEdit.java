package com.ys.springboot_blog_hodol.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : ysk
 */
@Getter
@Setter
public class PostEdit {

    private String title;

    private String content;

    @Builder
    public PostEdit(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
