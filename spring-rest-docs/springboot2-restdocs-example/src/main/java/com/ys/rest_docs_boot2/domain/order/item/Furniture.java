package com.ys.rest_docs_boot2.domain.order.item;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.util.Assert;

@Setter
@Getter
@Entity
@DiscriminatorValue("FURNITURE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Furniture extends Item {

    @Column(nullable = false)
    @ColumnDefault("0")
    private int width;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int height;

    public Furniture(int price, int stockQuantity, int width, int height) {
        super(price, stockQuantity);
        validateHeight(height);
        validateWidth(width);
        this.width = width;
        this.height = height;
    }

    private void validateWidth(int width) {
        Assert.isTrue(width > 0, "가로 길이는 0보다 작을 수 없습니다");
    }

    private void validateHeight(int height) {
        Assert.isTrue(height > 0, "세로 길이는 0보다 작을 수 없습니다");
    }

}
