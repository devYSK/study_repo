package com.fastcamedu.hellographql;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductQueryService {

	private final ProductRepository productRepository;

	public List<Product> findAll(Pageable pageable) {
		return productRepository.findAll(pageable).getContent();
	}

}
