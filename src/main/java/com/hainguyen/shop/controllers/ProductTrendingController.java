package com.hainguyen.shop.controllers;

import com.hainguyen.shop.dtos.response.ProductResponse;
import com.hainguyen.shop.services.IProductRedisTrendingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products/trending")
public class ProductTrendingController {

    private final IProductRedisTrendingService productTrendingService;

    @GetMapping()
    public ResponseEntity<Map<Double,ProductResponse>> topNProducts() {
        return ResponseEntity.ok()
                .body(this.productTrendingService.topNProducts(5));
    }
}
