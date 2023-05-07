package com.ys.cafekiosk.spring.api.service.order;

import static com.ys.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static com.ys.cafekiosk.spring.domain.product.ProductType.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.ys.cafekiosk.spring.api.service.order.request.OrderCreateRequest;
import com.ys.cafekiosk.spring.api.service.order.response.OrderCreateResponse;
import com.ys.cafekiosk.spring.domain.order.OrderRepository;
import com.ys.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import com.ys.cafekiosk.spring.domain.product.Product;
import com.ys.cafekiosk.spring.domain.product.ProductRepository;
import com.ys.cafekiosk.spring.domain.product.ProductType;
import com.ys.cafekiosk.spring.domain.stock.Stock;
import com.ys.cafekiosk.spring.domain.stock.StockRepository;

@SpringBootTest
@ActiveProfiles("test")
class OrderServiceTest {

	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderProductRepository orderProductRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private StockRepository stockRepository;

	@AfterEach
	void tearDown() {
		orderProductRepository.deleteAllInBatch();
		productRepository.deleteAllInBatch();
		orderRepository.deleteAllInBatch();
		stockRepository.deleteAllInBatch();
	}

	@DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
	@Test
	void createOrder() {
		//given
		Product product1 = createProduct(ProductType.HANDMADE, "001", 1000);
		Product product2 = createProduct(ProductType.HANDMADE, "002", 3000);
		Product product3 = createProduct(ProductType.HANDMADE, "003", 5000);
		productRepository.saveAll(List.of(product1, product2, product3));

		OrderCreateRequest request = OrderCreateRequest.builder()
			.productNumbers(List.of("001", "002"))
			.build();

		//when
		LocalDateTime now = LocalDateTime.now();
		OrderCreateResponse orderResponse = orderService.createOrder(request, now);

		assertThat(orderResponse.getId()).isNotNull();
		assertThat(orderResponse)
			.extracting("registeredDateTime", "totalPrice")
			.contains(now, 4000);
		assertThat(orderResponse.getProducts()).hasSize(2)
			.extracting("productNumber", "price")
			.containsExactlyInAnyOrder(
				tuple("001", 1000),
				tuple("002", 3000)
			);
	}

	@DisplayName("중복되는 프로덕트 넘버로 오더를 생성할 수 있다.")
	@Test
	void createOrderWithDuplicateProductNumbers() {
	    ///given
		Product product1 = createProduct(ProductType.HANDMADE, "001", 1000);
		Product product2 = createProduct(ProductType.HANDMADE, "002", 3000);
		Product product3 = createProduct(ProductType.HANDMADE, "003", 5000);
		productRepository.saveAll(List.of(product1, product2, product3));

		OrderCreateRequest request = OrderCreateRequest.builder()
			.productNumbers(List.of("001", "001"))
			.build();

		//when
		LocalDateTime now = LocalDateTime.now();
		OrderCreateResponse orderResponse = orderService.createOrder(request, now);

		assertThat(orderResponse.getId()).isNotNull();
		assertThat(orderResponse)
			.extracting("registeredDateTime", "totalPrice")
			.contains(now, 2000);
		assertThat(orderResponse.getProducts()).hasSize(2)
			.extracting("productNumber", "price")
			.containsExactlyInAnyOrder(
				tuple("001", 1000),
				tuple("001", 1000)
			);
	}


	@DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성한다.")
	@Test
	void createOrderWithStock() {
		// given
		LocalDateTime registeredDateTime = LocalDateTime.now();

		Product product1 = createProduct(BOTTLE, "001", 1000);
		Product product2 = createProduct(BAKERY, "002", 3000);
		Product product3 = createProduct(HANDMADE, "003", 5000);
		productRepository.saveAll(List.of(product1, product2, product3));

		Stock stock1 = Stock.create("001", 2);
		Stock stock2 = Stock.create("002", 2);
		stockRepository.saveAll(List.of(stock1, stock2));

		OrderCreateRequest request = OrderCreateRequest.builder()
			.productNumbers(List.of("001", "001", "002", "003"))
			.build();

		// when
		OrderCreateResponse orderResponse = orderService.createOrder(request, registeredDateTime);

		// then
		assertThat(orderResponse.getId()).isNotNull();
		assertThat(orderResponse)
			.extracting("registeredDateTime", "totalPrice")
			.contains(registeredDateTime, 10000);
		assertThat(orderResponse.getProducts()).hasSize(4)
			.extracting("productNumber", "price")
			.containsExactlyInAnyOrder(
				tuple("001", 1000),
				tuple("001", 1000),
				tuple("002", 3000),
				tuple("003", 5000)
			);

		List<Stock> stocks = stockRepository.findAll();
		assertThat(stocks).hasSize(2)
			.extracting("productNumber", "quantity")
			.containsExactlyInAnyOrder(
				tuple("001", 0),
				tuple("002", 1)
			);
	}

	@DisplayName("재고가 부족한 상품으로 주문을 생성하려는 경우 예외가 발생한다.")
	@Test
	void createOrderWithNoStock() {
		// given
		LocalDateTime registeredDateTime = LocalDateTime.now();

		Product product1 = createProduct(BOTTLE, "001", 1000);
		Product product2 = createProduct(BAKERY, "002", 3000);
		Product product3 = createProduct(HANDMADE, "003", 5000);
		productRepository.saveAll(List.of(product1, product2, product3));

		Stock stock1 = Stock.create("001", 2);
		Stock stock2 = Stock.create("002", 2);
		stock1.deductQuantity(1); // todo
		stockRepository.saveAll(List.of(stock1, stock2));

		OrderCreateRequest request = OrderCreateRequest.builder()
			.productNumbers(List.of("001", "001", "002", "003"))
			.build();

		// when // then
		assertThatThrownBy(() -> orderService.createOrder(request, registeredDateTime))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("재고가 부족한 상품이 있습니다.");
	}

	private Product createProduct(ProductType type, String productNumber, int price) {
		return Product.builder()
			.productNumber(productNumber)
			.type(type)
			.sellingStatus(SELLING)
			.name("메뉴 이름")
			.price(price)
			.build();
	}
}