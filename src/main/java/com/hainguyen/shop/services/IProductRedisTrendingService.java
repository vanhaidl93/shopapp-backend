package com.hainguyen.shop.services;

import com.hainguyen.shop.dtos.response.ProductResponse;

import java.util.Map;

public interface IProductRedisTrendingService {

    Map<Double,ProductResponse> topNProducts(int n);

    void addVisit(ProductResponse productResponse);
}
