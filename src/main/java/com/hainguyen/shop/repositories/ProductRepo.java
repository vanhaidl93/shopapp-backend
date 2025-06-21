package com.hainguyen.shop.repositories;

import com.hainguyen.shop.models.Product;
import org.hibernate.annotations.DialectOverride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {

   boolean existsByName(String name);

   @Query("SELECT p FROM Product p WHERE " +
           "(:categoryId IS NULL OR :categoryId =0 OR p.category.id = :categoryId) " +
           "AND (:keyword IS NULL OR :keyword = '' OR p.name LIKE %:keyword% OR p.description LIKE %:keyword%)")
   Page<Product> searchProducts(Long categoryId, String keyword, Pageable pageable);


   @Query("SELECT p FROM Product p WHERE p.id IN :productIds")
   List<Product> findProductsByProductIds(List<Long> productIds);

}
