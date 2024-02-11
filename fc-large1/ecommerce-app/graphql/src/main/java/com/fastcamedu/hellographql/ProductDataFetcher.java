package com.fastcamedu.hellographql;

import java.util.List;

import org.springframework.data.domain.PageRequest;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import lombok.RequiredArgsConstructor;

@DgsComponent
@RequiredArgsConstructor
public class ProductDataFetcher {
	private final ProductQueryService productQueryService;
	@DgsQuery
	public List<ProductDTO> products(@InputArgument Integer page, @InputArgument Integer size) {
		List<Product> products = productQueryService.findAll(PageRequest.of(page, size));

		return products.stream()
			.map(ProductDTO::of)
			.toList();

	}

}