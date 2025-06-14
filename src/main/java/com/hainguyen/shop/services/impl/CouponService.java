package com.hainguyen.shop.services.impl;


import com.hainguyen.shop.exceptions.ResourceNotFoundException;
import com.hainguyen.shop.models.Coupon;
import com.hainguyen.shop.models.CouponCondition;
import com.hainguyen.shop.repositories.CouponConditionRepository;
import com.hainguyen.shop.repositories.CouponRepository;
import com.hainguyen.shop.services.ICouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@RequiredArgsConstructor
@Service
public class CouponService implements ICouponService {

    private final CouponRepository couponRepository;
    private final CouponConditionRepository couponConditionRepository;

    @Override
    public double calculateCouponValue(String couponCode, double totalAmount) {

        Coupon coupon = couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon", "couponCode", couponCode));
        if (!coupon.isActive()) {
            throw new IllegalArgumentException("Coupon is not active");
        }
        double discount = calculateDiscount(coupon, totalAmount);

        return totalAmount - discount;
    }

    private double calculateDiscount(Coupon coupon, double totalAmount) {
        List<CouponCondition> conditions = couponConditionRepository.findByCouponId(coupon.getId());
        double discount = 0.0;
        double updatedTotalAmount = totalAmount;

        for (CouponCondition condition : conditions) {
            //EAV(Entity - Attribute - Value) Model
            String attribute = condition.getAttribute();
            String operator = condition.getOperator();
            String value = condition.getValue();
            double percentDiscount = Double.parseDouble(String.valueOf(condition.getDiscountAmount()));

            double discountPerCondition = switch (attribute) {
                case "minimum_amount" -> minimumCalculate(operator, value, percentDiscount, updatedTotalAmount);
                case "applicable_date" -> applicableDate(operator, value, percentDiscount, updatedTotalAmount);
                default -> 0;
            };
            updatedTotalAmount = updatedTotalAmount - discountPerCondition;
            discount += discountPerCondition;
        }

        return discount;
    }

    private double minimumCalculate(String operator, String value, double percentDiscount, double updatedTotalAmount) {
        if (operator.equals(">") && updatedTotalAmount > Double.parseDouble(value)) {
            return updatedTotalAmount * percentDiscount / 100;
        }
        return 0;
    }

    private double applicableDate(String operator, String value, double percentDiscount, double updatedTotalAmount) {
        LocalDate applicableDate = LocalDate.parse(value);
        LocalDate currentDate = LocalDate.now();
        if (operator.equalsIgnoreCase("BETWEEN") && currentDate.isEqual(applicableDate)) {
            return updatedTotalAmount * percentDiscount / 100;
        }
        return 0;
    }
}
