package com.ys.cafekiosk.spring.api.controller.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.cafekiosk.spring.api.service.order.OrderService;
import com.ys.cafekiosk.spring.api.service.order.request.OrderCreateRequest;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private OrderService orderService;

	@DisplayName("신규 주문을 등록한다.")
	@Test
	void createOrder() throws Exception {
		// given
		OrderCreateRequest request = OrderCreateRequest.builder()
			.productNumbers(List.of("001"))
			.build();

		// when // then
		mockMvc.perform(
				post("/api/v1/orders/new")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(jsonPath("$.status").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"));
		;
	}

	@DisplayName("신규 주문을 등록할 때 상품번호는 1개 이상이어야 한다.")
	@Test
	void createOrderWithEmptyProductNumbers() throws Exception {
		// given
		OrderCreateRequest request = OrderCreateRequest.builder()
			.productNumbers(List.of())
			.build();

		// when // then
		mockMvc.perform(
				post("/api/v1/orders/new")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.status").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("상품 번호 리스트는 필수입니다."))
			.andExpect(jsonPath("$.data").isEmpty())
		;
	}

}