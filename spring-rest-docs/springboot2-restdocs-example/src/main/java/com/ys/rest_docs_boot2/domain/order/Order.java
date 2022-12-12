package com.ys.rest_docs_boot2.domain.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ys.rest_docs_boot2.domain.base.AbstractTimeColumn;
import com.ys.rest_docs_boot2.domain.member.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.util.StringUtils;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends AbstractTimeColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "order_datetime", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime orderDatetime = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.OPENED;

    @Lob
    @Column(nullable = false)
    @ColumnDefault("'부재시 전화주세요.'")
    private String memo = "부재시 전화주세요.";

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    protected Order(String memo) {
        if (StringUtils.hasText(memo)) {
            this.memo = memo;
        }
    }

    public static Order create(String memo) {
        return new Order(memo);
    }

    public static Order createDefaultMessage() {
        return new Order();
    }

//    public void changeMember(Member member) {
//        if(Objects.nonNull(this.member)) {
//            this.member.getOrders().remove(this);
//        }
//
//        this.member = member;
//        member.getOrders().add(this);
//    }

    public void addOrderItem(OrderItem orderItem) {
        orderItem.changeOrder(this);
    }

}
