package com.hainguyen.shop.services;

public interface ICouponService {

    double calculateCouponValue(String couponCode, double totalAmount);
}
