package com.fastcampus.helloecommerceservice.domain.cart;

import com.fastcampus.helloecommerceservice.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "cart_items", schema = "ecommerce")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartItem extends BaseEntity {
    private static final int INIT_QUANTITY = 1;
    private  static final long INIT_CART_ITEM_ID = 0L;
    private  static final int DEFAULT_ADD_QUANTITY = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long cartItemId;
    @Column(name = "cart_id")
    private Long cartId;
    @Column(name = "product_id")
    private Long productId;
    @Column(name = "quantity")
    private int quantity = 0;

    public static CartItem of(Long cartId, Long productId) {
        CartItem cartItem = new CartItem(INIT_CART_ITEM_ID, cartId, productId, INIT_QUANTITY);
        return cartItem;
    }

    public void add() {
        this.quantity = this.quantity + DEFAULT_ADD_QUANTITY;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
