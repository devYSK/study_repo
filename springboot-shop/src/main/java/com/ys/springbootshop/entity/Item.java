package com.ys.springbootshop.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ys.springbootshop.constant.ItemSellStatus;
import com.ys.springbootshop.dto.ItemFormDto;
import com.ys.springbootshop.exception.OutOfStockException;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author : ysk
 */
@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Item extends BaseEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // 상품 코드

    @Column(nullable = false, length = 50)
    private String itemNm; // 상품명

    @Column(name = "price", nullable = false)
    private int price; // 가격

    @Column(nullable = false)
    private int stockNumber; // 재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail;// 상품 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; // 상품 판매 상태

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public void removeStock(int stockNumber) {
        int restStock = this.stockNumber - stockNumber;
        if (restStock < 0) {
            throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량 : " + this.stockNumber + ")");
        }

        this.stockNumber = stockNumber;
    }

    public void addStock(int stockNumber) {
        this.stockNumber += stockNumber;
    }


}
