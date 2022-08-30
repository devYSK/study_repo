package com.ys.jpabookstudy.member;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author : ysk
 */
@Entity
@Table(name = "MEMBER")
@Getter @Setter
public class Member {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "name")
    private String username;

    private Integer age;

}
