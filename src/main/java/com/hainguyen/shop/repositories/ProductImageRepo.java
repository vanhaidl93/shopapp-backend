package com.hainguyen.shop.repositories;

import com.hainguyen.shop.models.ProductImage;
import com.hainguyen.shop.services.IProductImageService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImageRepo
        extends JpaRepository<ProductImage, Long> {

    Optional<ProductImage> findByImageName(String productImageName);

    List<ProductImage> findByProductId(Long productId);
}
