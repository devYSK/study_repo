package com.ys.jpabookstudy.member;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author : ysk
 */
@Entity
@Setter
@Getter
public class Locker {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOCKER_ID")
    private Long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
}
