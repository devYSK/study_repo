package com.ys.springbootshop.dto;

import com.ys.springbootshop.constant.ItemSellStatus;
import com.ys.springbootshop.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : ysk
 */
@Getter
@Setter
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "상품 상세는 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();


    public Item createItem(ModelMapper modelMapper){
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item, ModelMapper modelMapper){
        return modelMapper.map(item,ItemFormDto.class);
    }

}