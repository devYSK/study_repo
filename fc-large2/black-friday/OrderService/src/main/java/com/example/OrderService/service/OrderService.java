package com.example.OrderService.service;

import blackfriday.protobuf.EdaMessage;
import com.example.OrderService.dto.*;
import com.example.OrderService.entity.ProductOrder;
import com.example.OrderService.enums.OrderStatus;
import com.example.OrderService.feign.CatalogClient;
import com.example.OrderService.feign.DeliveryClient;
import com.example.OrderService.feign.PaymentClient;
import com.example.OrderService.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    PaymentClient paymentClient;

    @Autowired
    DeliveryClient deliveryClient;

    @Autowired
    CatalogClient catalogClient;

    @Autowired
    private KafkaTemplate<String, byte[]> kafkaTemplate;

    public StartOrderResponseDto startOrder(Long userId, Long productId, Long count) {
        // 1. 상품 정보 조회
        var product = catalogClient.getProduct(productId);

        // 2. 결제 수단 정보 조회
        var paymentMethod = paymentClient.getPaymentMethod(userId);

        // 3. 배송지 정보 조회
        var address = deliveryClient.getUserAddress(userId);

        // 4. 주문 정보 생성
        var order = new ProductOrder(
                userId,
                productId,
                count,
                OrderStatus.INITIATED,
                null,
                null,
                null
        );
        orderRepository.save(order);

        var startOrderDto = new StartOrderResponseDto();
        startOrderDto.orderId = order.id;
        startOrderDto.paymentMethod = paymentMethod;
        startOrderDto.address = address;

        return startOrderDto;
    }

    public ProductOrder finishOrder(Long orderId, Long paymentMethodId, Long addressId) {
        var order = orderRepository.findById(orderId).orElseThrow();

        // 1. 상품 정보 조회
        var product = catalogClient.getProduct(order.productId);

        // 2. 결제 요청
        var message = EdaMessage.PaymentRequestV1.newBuilder()
                .setOrderId(order.id)
                .setUserId(order.userId)
                .setAmountKRW(Long.parseLong(product.get("price").toString()) * order.count)
                .setPaymentMethodId(paymentMethodId)
                .build();

        kafkaTemplate.send("payment_request", message.toByteArray());

        // 3. 주문 정보 업데이트
        var address = deliveryClient.getAddress(addressId);
        order.orderStatus = OrderStatus.PAYMENT_REQUESTED;
        order.deliveryAddress = address.get("address").toString();
        return orderRepository.save(order);
    }

    public List<ProductOrder> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public ProductOrderDetailDto getOrderDetail(Long orderId) {
        var order = orderRepository.findById(orderId).orElseThrow();

        var paymentRes = paymentClient.getPayment(order.paymentId);
        var deliveryRes = deliveryClient.getDelivery(order.deliveryId);

        var dto = new ProductOrderDetailDto(
                order.id,
                order.userId,
                order.productId,
                order.paymentId,
                order.deliveryId,
                order.orderStatus,
                paymentRes.get("paymentStatus").toString(),
                deliveryRes.get("status").toString()
        );

        return dto;
    }
}
