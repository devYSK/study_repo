package com.ys.common.service;

import org.springframework.stereotype.Service;

@Service
public class CommonDemoService {

	public String commonService() {
		return "commonService";
	}

	public String getModuleName() {
		return "common-module";
	}
}
