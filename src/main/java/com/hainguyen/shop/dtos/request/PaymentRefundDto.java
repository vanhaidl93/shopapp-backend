package com.hainguyen.shop.dtos.request;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PaymentRefundDto(String transactionType,
                               Long orderId,
                               Long amount,
                               LocalDate transactionDate,
                               LocalDate createBy,
                               String ipAddress) {
}
