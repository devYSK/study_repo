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
@DiscriminatorValue("CAR")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Car extends Item {

    @Column(nullable = false)
    @ColumnDefault("0")
    private int power;

    public Car(int price, int stockQuantity, int power) {
        super(price, stockQuantity);
        validatePower(power);
        this.power = power;
    }

    private void validatePower(int power) {
        Assert.isTrue(power > 0, "power 는 0보다 작을 수 없습니다");
    }

}
