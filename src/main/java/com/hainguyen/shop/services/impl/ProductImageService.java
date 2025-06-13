package com.hainguyen.shop.services.impl;

import com.hainguyen.shop.exceptions.ResourceNotFoundException;
import com.hainguyen.shop.models.ProductImage;
import com.hainguyen.shop.repositories.ProductImageRepository;
import com.hainguyen.shop.services.IProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductImageService implements IProductImageService {

    private final ProductImageRepository productImageRepository;

    @Override
    public ProductImage deleteProductImage(Long id) {
        ProductImage productImage = productImageRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("ProductImage","Id",id.toString()));

        productImageRepository.deleteById(id);
        return productImage;
    }
}
