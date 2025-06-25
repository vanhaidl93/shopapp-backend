package com.hainguyen.shop.dtos.request;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PaymentQueryDto(Long orderId,
                              LocalDate transactionDate,
                              String ipAddress) {
}
