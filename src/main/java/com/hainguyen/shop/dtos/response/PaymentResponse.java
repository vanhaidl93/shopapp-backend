package com.hainguyen.shop.dtos.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record PaymentResponse(String message,
                              HttpStatus status,
                              String data) {
}
