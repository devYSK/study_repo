package com.example.settlementforkafka;

import lombok.Data;

@Data
public class OrderCompletedMessage {
    public String txId;
    public String orderId;
    public String customerId;
    public String completedAt;
    public String version;
}
