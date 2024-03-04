package com.ys.catalogservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ys.catalogservice.jpa.CatalogEntity;
import com.ys.catalogservice.jpa.CatalogRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService{
    private final CatalogRepository catalogRepository;

    @Override
    public List<CatalogEntity> getAllCatalogs() {
        return catalogRepository.findAll();
    }
}
