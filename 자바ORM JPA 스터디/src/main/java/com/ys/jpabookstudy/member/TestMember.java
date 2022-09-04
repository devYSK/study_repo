package com.ys.jpabookstudy.member;

import lombok.AllArgsConstructor;

import javax.persistence.Entity;

/**
 * @author : ysk
 */
@Entity
@AllArgsConstructor
public class TestMember {

    private Long id;

    private String email;
}
