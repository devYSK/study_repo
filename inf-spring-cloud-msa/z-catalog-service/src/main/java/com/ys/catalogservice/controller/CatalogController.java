package com.ys.catalogservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ys.catalogservice.dto.ResponseCatalog;
import com.ys.catalogservice.jpa.CatalogEntity;
import com.ys.catalogservice.service.CatalogService;

@RestController
@RequestMapping("/catalog-service")
public class CatalogController {
	Environment env;
	CatalogService catalogService;

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	public CatalogController(Environment env, CatalogService catalogService) {
		this.env = env;
		this.catalogService = catalogService;
	}

	@GetMapping("/health_check")
	public String status() {
		List<ServiceInstance> serviceList = getApplications();
		for (ServiceInstance instance : serviceList) {
			System.out.println(String.format("instanceId:%s, serviceId:%s, host:%s, scheme:%s, uri:%s",
				instance.getInstanceId(), instance.getServiceId(), instance.getHost(), instance.getScheme(),
				instance.getUri()));
		}

		return String.format("It's Working in Catalog Service on LOCAL PORT %s (SERVER PORT %s)",
			env.getProperty("local.server.port"),
			env.getProperty("server.port"));
	}

	@GetMapping("/catalogs")
	public ResponseEntity<List<ResponseCatalog>> getCatalogs() {
		return ResponseEntity.status(HttpStatus.OK)
			.body(
				catalogService.getAllCatalogs()
					.stream()
					.map(
						it -> new ModelMapper().map(it, ResponseCatalog.class))
					.toList()
			);
	}

	private List<ServiceInstance> getApplications() {

		List<String> services = this.discoveryClient.getServices();
		List<ServiceInstance> instances = new ArrayList<ServiceInstance>();
		services.forEach(serviceName -> instances.addAll(this.discoveryClient.getInstances(serviceName)));

		return instances;
	}
}
