package com.ys.rest_docs_boot2.domain.order.item;

import com.ys.rest_docs_boot2.domain.base.AbstractTimeColumn;
import com.ys.rest_docs_boot2.domain.order.OrderItem;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.util.Assert;

@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dis")
@Table(name = "item")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Item extends AbstractTimeColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int price;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int stockQuantity;

    @ManyToOne
    @JoinColumn(name = "order_item_id", referencedColumnName = "id")
    private OrderItem orderItem;

    public void changeOrderItem(OrderItem orderItem) {
        if (Objects.nonNull(this.orderItem)) {
            this.orderItem.getItems().remove(this);
        }

        this.orderItem = orderItem;
        orderItem.getItems().add(this);
    }

    protected Item(int price, int stockQuantity) {
        validatePrice(price);
        validateQuantity(stockQuantity);
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    private void validatePrice(int price) {
        Assert.isTrue(price > 0, "가격은 0보다 작을 수 없습니다");
    }

    private void validateQuantity(int quantity) {
        Assert.isTrue(quantity > 0, "수량은 0보다 작을 수 없습니다");
    }

}
