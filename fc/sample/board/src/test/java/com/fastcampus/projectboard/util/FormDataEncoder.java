package com.fastcampus.projectboard.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@TestComponent
public class FormDataEncoder {

	@Autowired
	private ObjectMapper mapper;

	public String encode(Object obj) {
		Map<String, String> fieldMap = mapper.convertValue(obj, new TypeReference<>() {
		});
		MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
		valueMap.setAll(fieldMap);

		return UriComponentsBuilder.newInstance()
								   .queryParams(valueMap)
								   .encode()
								   .build()
								   .getQuery();
	}

}
