package com.hainguyen.shop.repositories;

import com.hainguyen.shop.models.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepo
        extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductId(Long productId);
}
