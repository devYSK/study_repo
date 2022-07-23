package com.ys.servlet.domain;

import lombok.*;

/**
 * @author : ysk
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Member {

    private Long id;
    private String username;
    private int age;

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
