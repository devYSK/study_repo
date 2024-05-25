package com.example.OrderService;

import com.example.OrderService.entity.ProductOrder;
import com.example.OrderService.enums.OrderStatus;
import com.example.OrderService.feign.CatalogClient;
import com.example.OrderService.feign.DeliveryClient;
import com.example.OrderService.feign.PaymentClient;
import com.example.OrderService.repository.OrderRepository;
import com.example.OrderService.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import(OrderService.class)
public class OrderServiceTest {

    @SpyBean
    OrderRepository orderRepository;

    @MockBean
    PaymentClient paymentClient;

    @MockBean
    DeliveryClient deliveryClient;

    @MockBean
    CatalogClient catalogClient;

    @MockBean
    private KafkaTemplate<String, byte[]> kafkaTemplate;

    @Autowired
    OrderService orderService;

    @Test
    void startOrderTest() {
        // given
        var paymentMethodRes = new HashMap<String, Object>();
        var userAddressRes = new HashMap<String, Object>();
        paymentMethodRes.put("paymentMethodType", "CREDIT_CARD");
        userAddressRes.put("address", "경기도 성남시");

        when(paymentClient.getPaymentMethod(1L)).thenReturn(paymentMethodRes);
        when(deliveryClient.getUserAddress(1L)).thenReturn(userAddressRes);

        // when
        var startOrderResponseDto = orderService.startOrder(1L, 1L, 2L);

        // then
        assertNotNull(startOrderResponseDto.orderId);
        assertEquals(paymentMethodRes, startOrderResponseDto.paymentMethod);
        assertEquals(userAddressRes, startOrderResponseDto.address);

        var order = orderRepository.findById(startOrderResponseDto.orderId);
        assertEquals(OrderStatus.INITIATED, order.get().orderStatus);
    }

    @Test
    void finishOrderTest() {
        // given
        var orderStarted = new ProductOrder(
                1L,
                1L,
                1L,
                OrderStatus.INITIATED,
                null,
                null,
                null
        );
        orderRepository.save(orderStarted);

        final var address = "경기도 성남시";

        var catalogResponse = new HashMap<String, Object>();
        var deliveryResponse = new HashMap<String, Object>();
        catalogResponse.put("price", "100");
        deliveryResponse.put("address", address);

        when(catalogClient.getProduct(orderStarted.productId)).thenReturn(catalogResponse);
        when(deliveryClient.getAddress(1L)).thenReturn(deliveryResponse);

        // when
        var response = orderService.finishOrder(orderStarted.id, 1L, 1L);

        // then
        assertEquals(address, response.deliveryAddress);
        verify(kafkaTemplate, times(1)).send(
                eq("payment_request"),
                any(byte[].class)
        );
    }

    @Test
    void getUserOrdersTest() {
        // given
        final var userId = 123L;

        var order1 = new ProductOrder(
                userId,
                100L,
                1L,
                OrderStatus.INITIATED,
                null,
                null,
                null
        );

        var order2 = new ProductOrder(
                userId,
                110L,
                1L,
                OrderStatus.INITIATED,
                null,
                null,
                null
        );

        orderRepository.save(order1);
        orderRepository.save(order2);

        // when
        var response = orderService.getUserOrders(userId);

        // then
        assertEquals(2, response.size());
        assertEquals(100L, response.get(0).productId);
        assertEquals(110L, response.get(1).productId);
    }

    @Test
    void getOrderDetailTest() {
        //given
        var productOrder = new ProductOrder(
                1L,
                1L,
                1L,
                OrderStatus.DELIVERY_REQUESTED,
                10L,
                11L,
                null
        );

        orderRepository.save(productOrder);

        final var paymentStatus = "COMPLETED";
        final var deliveryStatus = "IN_DELIVERY";

        var paymentResponse = new HashMap<String, Object>();
        var deliveryResponse = new HashMap<String, Object>();
        paymentResponse.put("paymentStatus", paymentStatus);
        deliveryResponse.put("status", deliveryStatus);

        when(orderRepository.findById(1000L)).thenReturn(Optional.of(productOrder));
        when(paymentClient.getPayment(10L)).thenReturn(paymentResponse);
        when(deliveryClient.getDelivery(11L)).thenReturn(deliveryResponse);

        // when
        var response = orderService.getOrderDetail(1000L);

        // then
        assertEquals(10L, response.paymentId);
        assertEquals(11L, response.deliveryId);
        assertEquals(paymentStatus, response.paymentStatus);
        assertEquals(deliveryStatus, response.deliveryStatus);
    }
}
