package com.hainguyen.shop.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hainguyen.shop.dtos.response.ProductsResponsePage;
import org.springframework.data.domain.PageRequest;

public interface IProductRedisService {

    void clear();

    ProductsResponsePage getProductsPerPage(PageRequest pageRequest);

    void saveProductsPerPage(ProductsResponsePage productResponsesPage, PageRequest pageRequest);

}
