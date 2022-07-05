package com.ys.springbootshop.dto;

import com.ys.springbootshop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : ysk
 */
@Getter @Setter
public class ItemSearchDto {

    private String searchDateType;

    private ItemSellStatus searchSellStatus;

    private String searchBy;

    private String searchQuery = "";
}
