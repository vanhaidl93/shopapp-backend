package com.hainguyen.shop.dtos.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class OrderResponse {

    private Long id;

    private Long userId;

    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private String note;
    private Float totalMoney;
    private String shippingMethod;
    private String shippingAddress;
    private String paymentMethod;

    private LocalDate orderDate;
    private String status;
    private LocalDate shippingDate;
    private String trackingNumber;
    private boolean active;

    private List<OrderDetailResponse> orderDetailsResponse;
}
