package com.hainguyen.shop.controllers;


import com.hainguyen.shop.dtos.response.ActualAmountResponse;
import com.hainguyen.shop.services.ICouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final ICouponService couponService;

    @GetMapping("/calculate")
    public ResponseEntity<ActualAmountResponse> calculateCouponValue(@RequestParam String couponCode,
                                                                     @RequestParam double totalAmount) {
        double actualAmount = couponService.calculateCouponValue(couponCode, totalAmount);

        return ResponseEntity.ok()
                .body(ActualAmountResponse.builder().actualAmount(actualAmount).build());
    }
}
