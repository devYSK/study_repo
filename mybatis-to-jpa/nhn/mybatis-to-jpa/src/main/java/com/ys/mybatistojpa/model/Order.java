package com.ys.mybatistojpa.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orderId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+09:00")
    private Date orderDate;

    private List<OrderItem> orderItems;

}
