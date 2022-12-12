package com.ys.rest_docs_boot2.domain.order;

import com.ys.rest_docs_boot2.domain.base.AbstractTimeColumn;
import com.ys.rest_docs_boot2.domain.order.item.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.util.Assert;

@Entity
@Table(name = "order_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends AbstractTimeColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int price;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL)
    private List<Item> items = new ArrayList<>();

    public OrderItem(int price, int quantity) {
        validatePrice(price);
        validateQuantity(quantity);
        this.price = price;
        this.quantity = quantity;
    }

    private void validatePrice(int price) {
        Assert.isTrue(price > 0, "가격은 0보다 작을 수 없습니다");
    }

    private void validateQuantity(int quantity) {
        Assert.isTrue(quantity > 0, "수량은 0보다 작을 수 없습니다");
    }

    public void changeOrder(Order order) {
        if (Objects.nonNull(this.order)) {
            this.order.getOrderItems().remove(this);
        }

        this.order = order;
        order.getOrderItems().add(this);
    }

    public void addItem(Item item) {
        item.changeOrderItem(this);
    }

}
