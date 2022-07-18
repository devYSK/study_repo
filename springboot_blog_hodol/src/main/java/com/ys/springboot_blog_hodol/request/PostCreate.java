package com.ys.springboot_blog_hodol.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * @author : ysk
 */
@Getter @ToString
@NoArgsConstructor @AllArgsConstructor @Builder
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해주세요.")
    private String title;

    @NotBlank(message = "컨텐츠를 입력해주세요.")
    private String content;

    public PostCreate changeTitle(String title) {
        return PostCreate.builder()
                .title(title)
                .content(content)
                .build();
    }
}
