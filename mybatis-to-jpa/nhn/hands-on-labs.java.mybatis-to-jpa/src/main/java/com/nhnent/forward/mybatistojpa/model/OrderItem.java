package com.nhnent.forward.mybatistojpa.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItem {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orderId;

    private Integer lineNumber;

    private Integer quantity;

    private Item item;

}
