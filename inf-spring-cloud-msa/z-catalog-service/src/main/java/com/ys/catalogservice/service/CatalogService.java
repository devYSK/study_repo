package com.ys.catalogservice.service;

import java.util.List;

import com.ys.catalogservice.jpa.CatalogEntity;

public interface CatalogService {
	List<CatalogEntity> getAllCatalogs();
}
