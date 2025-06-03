package com.hainguyen.shop.dtos.response;

import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrdersResponsePage {
    private List<OrderResponse> ordersResponse;
    private int currentPage;
    private int totalPages;
}
