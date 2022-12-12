package com.ys.rest_docs_boot2.domain.order.item;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.Assert;

@Setter
@Getter
@Entity
@DiscriminatorValue("FOOD")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Food extends Item {

    @Column(nullable = false)
    private String chef;

    public Food(int price, int stockQuantity, String chef) {
        super(price, stockQuantity);
        validateChef(chef);
        this.chef = chef;
    }

    private void validateChef(String chef) {
        Assert.hasText(chef, "chef는 빈 값이면 안됩니다.");
    }

}
