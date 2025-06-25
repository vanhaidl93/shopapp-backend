package com.hainguyen.shop.repositories;

import com.hainguyen.shop.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order,Long> {

    List<Order> findByUserId(Long userId);

    @Query("SELECT o FROM Order o WHERE " +
            "o.active =TRUE AND  " +
            "(:keyword IS NULL " +
            "OR :keyword ='' " +
            "OR o.fullName LIKE %:keyword% " +
            "OR o.address LIKE %:keyword% " +
            "OR o.note LIKE %:keyword%)")
    Page<Order> searchOrdersByKeyword(String keyword, Pageable pageable);

    Optional<Order> findByVnpTxnRef(String vnpTxnRef);
}
