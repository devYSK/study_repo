package com.fastcampus.helloorderapi.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderCompletedMessage {
    public String txId;
    public String orderId;
    public String customerId;
    public String completedAt;
    public String version;
}
