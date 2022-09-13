package com.ys.jpabookstudy.member;

import lombok.AllArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

/**
 * @author : ysk
 */
@Entity
@AllArgsConstructor
public class TestMember {

    @GeneratedValue()
    private Long id;

    private String email;
}
