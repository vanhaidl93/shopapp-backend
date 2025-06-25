package com.hainguyen.shop.dtos.request;

import lombok.Builder;

@Builder
public record PaymentDto(Long amount,
                         String bankCode,
                         String language) {
}
