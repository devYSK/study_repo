package com.ys.cafekiosk.spring.api.service.order;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ys.cafekiosk.spring.api.service.order.request.OrderCreateRequest;
import com.ys.cafekiosk.spring.api.service.order.response.OrderCreateResponse;
import com.ys.cafekiosk.spring.domain.order.Order;
import com.ys.cafekiosk.spring.domain.order.OrderRepository;
import com.ys.cafekiosk.spring.domain.product.Product;
import com.ys.cafekiosk.spring.domain.product.ProductRepository;
import com.ys.cafekiosk.spring.domain.product.ProductType;
import com.ys.cafekiosk.spring.domain.stock.Stock;
import com.ys.cafekiosk.spring.domain.stock.StockRepository;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class OrderService {

	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;
	private final StockRepository stockRepository;

	public OrderCreateResponse createOrder(OrderCreateRequest request, LocalDateTime registeredDateTime) {
		List<String> productNumbers = request.getProductNumbers();
		List<Product> products = findProductsBy(productNumbers);

		deductStockQuantities(products);

		Order order = Order.create(products, registeredDateTime);
		Order savedOrder = orderRepository.save(order);
		return OrderCreateResponse.of(savedOrder);
	}

	private void deductStockQuantities(List<Product> products) {
		List<String> stockProductNumbers = extractStockProductNumbers(products);

		Map<String, Stock> stockMap = createStockMapBy(stockProductNumbers);
		Map<String, Long> productCountingMap = createCountingMapBy(stockProductNumbers);

		for (String stockProductNumber : new HashSet<>(stockProductNumbers)) {
			Stock stock = stockMap.get(stockProductNumber);
			int quantity = productCountingMap.get(stockProductNumber).intValue();

			if (stock.isQuantityLessThan(quantity)) {
				throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
			}
			stock.deductQuantity(quantity);
		}
	}

	private List<Product> findProductsBy(List<String> productNumbers) {
		List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
		Map<String, Product> productMap = products.stream()
			.collect(Collectors.toMap(Product::getProductNumber, p -> p));

		return productNumbers.stream()
			.map(productMap::get)
			.collect(Collectors.toList());
	}

	private static List<String> extractStockProductNumbers(List<Product> products) {
		return products.stream()
			.filter(product -> ProductType.containsStockType(product.getType()))
			.map(Product::getProductNumber)
			.collect(Collectors.toList());
	}

	private Map<String, Stock> createStockMapBy(List<String> stockProductNumbers) {
		List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);
		return stocks.stream()
			.collect(Collectors.toMap(Stock::getProductNumber, s -> s));
	}

	private static Map<String, Long> createCountingMapBy(List<String> stockProductNumbers) {
		return stockProductNumbers.stream()
			.collect(Collectors.groupingBy(p -> p, Collectors.counting()));
	}

}