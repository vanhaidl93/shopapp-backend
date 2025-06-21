package com.hainguyen.shop.repositories;


import com.hainguyen.shop.models.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepo extends JpaRepository<Coupon, Long> {

    Optional<Coupon> findByCode(String couponCode);
}