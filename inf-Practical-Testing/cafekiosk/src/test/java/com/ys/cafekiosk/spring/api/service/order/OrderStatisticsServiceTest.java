package com.ys.cafekiosk.spring.api.service.order;

import static com.ys.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static com.ys.cafekiosk.spring.domain.product.ProductType.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.ys.cafekiosk.spring.client.mail.MailSendClient;
import com.ys.cafekiosk.spring.domain.history.mail.MailSendHistory;
import com.ys.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;
import com.ys.cafekiosk.spring.domain.order.Order;
import com.ys.cafekiosk.spring.domain.order.OrderRepository;
import com.ys.cafekiosk.spring.domain.order.OrderStatus;
import com.ys.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import com.ys.cafekiosk.spring.domain.product.Product;
import com.ys.cafekiosk.spring.domain.product.ProductRepository;
import com.ys.cafekiosk.spring.domain.product.ProductType;

@SpringBootTest
class OrderStatisticsServiceTest {

	@Autowired
	private OrderStatisticsService orderStatisticsService;

	@Autowired
	private OrderProductRepository orderProductRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private MailSendHistoryRepository mailSendHistoryRepository;

	@MockBean
	private MailSendClient mailSendClient;

	@AfterEach
	void tearDown() {
		orderProductRepository.deleteAllInBatch();
		orderRepository.deleteAllInBatch();
		productRepository.deleteAllInBatch();
		mailSendHistoryRepository.deleteAllInBatch();
	}

	@DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
	@Test
	void sendOrderStatisticsMail() {
		// given
		LocalDateTime now = LocalDateTime.of(2023, 3, 5, 0, 0);

		Product product1 = createProduct(HANDMADE, "001", 1000);
		Product product2 = createProduct(HANDMADE, "002", 2000);
		Product product3 = createProduct(HANDMADE, "003", 3000);
		List<Product> products = List.of(product1, product2, product3);
		productRepository.saveAll(products);

		Order order1 = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 4, 23, 59, 59), products);
		Order order2 = createPaymentCompletedOrder(now, products);
		Order order3 = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 5, 23, 59, 59), products);
		Order order4 = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 6, 0, 0), products);

		// stubbing
		when(mailSendClient.sendEmail(any(String.class), any(String.class), any(String.class), any(String.class)))
			.thenReturn(true);

		// when
		boolean result = orderStatisticsService.sendOrderStatisticsMail(LocalDate.of(2023, 3, 5), "test@test.com");

		// then
		assertThat(result).isTrue();

		List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
		assertThat(histories).hasSize(1)
			.extracting("content")
			.contains("총 매출 합계는 12000원입니다.");
	}

	private Order createPaymentCompletedOrder(LocalDateTime now, List<Product> products) {
		Order order = Order.builder()
			.products(products)
			.orderStatus(OrderStatus.PAYMENT_COMPLETED)
			.registeredDateTime(now)
			.build();
		return orderRepository.save(order);
	}

	private Product createProduct(ProductType type, String productNumber, int price) {
		return Product.builder()
			.type(type)
			.productNumber(productNumber)
			.price(price)
			.sellingStatus(SELLING)
			.name("메뉴 이름")
			.build();
	}

}
