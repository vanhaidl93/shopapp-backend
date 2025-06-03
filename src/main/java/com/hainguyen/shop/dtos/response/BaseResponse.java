package com.hainguyen.shop.dtos.response;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter @Setter
public class BaseResponse {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
