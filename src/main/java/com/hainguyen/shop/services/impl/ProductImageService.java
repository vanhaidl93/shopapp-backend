package com.hainguyen.shop.services.impl;

import com.hainguyen.shop.exceptions.ResourceNotFoundException;
import com.hainguyen.shop.models.ProductImage;
import com.hainguyen.shop.repositories.ProductImageRepo;
import com.hainguyen.shop.services.IProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductImageService implements IProductImageService {

    private final ProductImageRepo productImageRepository;

    @Override
    public ProductImage deleteProductImage(String productImageName) {
        ProductImage productImage = productImageRepository.findByImageName(productImageName)
                        .orElseThrow(() -> new ResourceNotFoundException("ProductImage","imageName",productImageName));

        productImageRepository.delete(productImage);
        return productImage;
    }
}
