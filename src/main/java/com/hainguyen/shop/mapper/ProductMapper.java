package com.hainguyen.shop.mapper;

import com.hainguyen.shop.models.Product;
import com.hainguyen.shop.dtos.response.CategoryResponse;
import com.hainguyen.shop.dtos.response.ProductImageResponse;
import com.hainguyen.shop.dtos.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final ModelMapper modelMapper;

    public ProductResponse mapToProductResponse(Product product, ProductResponse productResponse) {
        CategoryResponse categoryResponse = modelMapper.map(product.getCategory(), CategoryResponse.class);
        List<ProductImageResponse> productImagesResponse = product.getProductImages().stream()
                .map(productImage -> modelMapper.map(productImage,ProductImageResponse.class))
                .toList();

        productResponse = modelMapper.map(product, ProductResponse.class);
        productResponse.setCategoryResponse(categoryResponse);
        productResponse.setProductImagesResponse(productImagesResponse);

        return productResponse;
    }
}
