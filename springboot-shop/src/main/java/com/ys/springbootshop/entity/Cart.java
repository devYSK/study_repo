package com.ys.springbootshop.entity;

import lombok.*;

import javax.persistence.*;

/**
 * @author : ysk
 */
@Entity
@Table(name = "cart")
@Getter @Setter
@ToString @NoArgsConstructor
@Builder @AllArgsConstructor
public class Cart extends BaseEntity {

    @Id
    @Column(name = "card_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static Cart createCart(Member member) {
        return Cart.builder()
                .member(member)
                .build();

    }

}
