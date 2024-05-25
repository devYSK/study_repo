package com.example.OrderService;

import blackfriday.protobuf.EdaMessage;
import com.example.OrderService.dto.DecreaseStockCountDto;
import com.example.OrderService.entity.ProductOrder;
import com.example.OrderService.enums.OrderStatus;
import com.example.OrderService.feign.CatalogClient;
import com.example.OrderService.repository.OrderRepository;
import com.example.OrderService.service.EventListener;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import(EventListener.class)
public class EventListenerTest {

    @SpyBean
    OrderRepository orderRepository;

    @MockBean
    CatalogClient catalogClient;

    @MockBean
    private KafkaTemplate<String, byte[]> kafkaTemplate;

    @Autowired
    EventListener eventListener;

    @Test
    void consumePaymentResultTest() throws Exception {
        // given
        var productId = 111L;
        var paymentId = 222L;

        var productOrder = new ProductOrder(
                1L,
                productId,
                1L,
                OrderStatus.INITIATED,
                null,
                null,
                "경기도 수원시"
        );
        var order = orderRepository.save(productOrder);

        var paymentResultMessage = EdaMessage.PaymentResultV1.newBuilder()
                .setOrderId(order.id)
                .setPaymentId(paymentId)
                .setPaymentStatus("COMPLETED")
                .build();

        var catalogResponse = new HashMap<String, Object>();
        catalogResponse.put("name", "Hello TV");

        when(catalogClient.getProduct(productId)).thenReturn(catalogResponse);

        // when
        eventListener.consumePaymentResult(paymentResultMessage.toByteArray());

        // then
        verify(kafkaTemplate, times(1)).send(
                eq("delivery_request"),
                any(byte[].class)
        );
        assertEquals(paymentId, order.paymentId);
    }


    @Captor
    ArgumentCaptor<DecreaseStockCountDto> captor;

    @Test
    void consumeDeliveryStatusUpdateTest() throws Exception {
        //given
        var productId = 111L;
        var deliveryId = 333L;
        var productCount = 10L;

        var productOrder = new ProductOrder(
                1L,
                productId,
                productCount,
                OrderStatus.INITIATED,
                null,
                null,
                "경기도 수원시"
        );
        var order = orderRepository.save(productOrder);

        var deliveryStatusUpdateMessage = EdaMessage.DeliveryStatusUpdateV1.newBuilder()
                .setOrderId(order.id)
                .setDeliveryStatus("REQUESTED")
                .setDeliveryId(deliveryId)
                .build();

        //when
        eventListener.consumeDeliveryStatusUpdate(deliveryStatusUpdateMessage.toByteArray());

        // then
        assertEquals(deliveryId, order.deliveryId);
        verify(catalogClient, times(1)).decreaseStockCount(
                eq(productId),
                captor.capture()
        );
        assertEquals(productCount, captor.getValue().decreaseCount);
    }

    @Test
    void consumeDeliveryStatusUpdateTest_not_REQUESTED() throws Exception {
        // given
        var deliveryStatusUpdateMessage = EdaMessage.DeliveryStatusUpdateV1.newBuilder()
                .setOrderId(1L)
                .setDeliveryStatus("IN_DELIVERY")
                .setDeliveryId(10L)
                .build();

        // when
        eventListener.consumeDeliveryStatusUpdate(deliveryStatusUpdateMessage.toByteArray());

        // then
        verify(orderRepository, times(0)).save(any());
        verify(catalogClient, times(0)).decreaseStockCount(any(), any());
    }
}
