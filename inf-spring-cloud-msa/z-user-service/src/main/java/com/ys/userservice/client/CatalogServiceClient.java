package com.ys.userservice.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.ys.userservice.config.FeignConfig;
import com.ys.userservice.dto.ResponseCatalog;

@FeignClient(name="catalog-service", configuration = FeignConfig.CatalogFeignErrorDecoder.class)
public interface CatalogServiceClient {

    @GetMapping("/catalog-service/catalogs")
    List<ResponseCatalog> getCatalogs();

}
