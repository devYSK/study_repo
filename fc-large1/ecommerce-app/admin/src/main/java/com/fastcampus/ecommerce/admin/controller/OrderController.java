package com.fastcampus.ecommerce.admin.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fastcampus.ecommerce.admin.exception.NotFoundOrderException;
import com.fastcampus.ecommerce.admin.infrastructure.adapter.OrderAdapter;
import com.fastcampus.ecommerce.admin.service.OrderService;
import com.fastcampus.ecommerce.admin.service.dto.OrderDTO;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class OrderController {

	public static final String MENU_KEY = "orders";
	private final OrderService orderService;
	private final OrderAdapter orderAdapter;

	@GetMapping({"/orders/", "/orders"})
	public String index(
		@PageableDefault(size = 20, sort = "orderId", direction = Sort.Direction.DESC) Pageable pageable,
		Model model) {

		// List<OrderDetailView> orderDetailViews = orderService.findAllOrderDetailView();
		List<OrderDTO> orderDTOs= orderAdapter.findAll(pageable);
		model.addAttribute("orders", orderDTOs);

		return "/orders/orders";
	}

	@GetMapping("/orders/order-detail")
	public String detail(@RequestParam Long orderId, Model model) {
		Optional<OrderDTO> optionalOrderDTO = orderService.findById(orderId);
		OrderDTO orderDTO = optionalOrderDTO.orElseThrow(() -> new NotFoundOrderException("Not found order info"));
		model.addAttribute("order", orderDTO);
		model.addAttribute("menuId", MENU_KEY);
		return "/orders/order-detail";
	}

}
