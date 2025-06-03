package com.hainguyen.shop.dtos.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class OrderDetailResponse {


    private long id;

    private long orderId;

    private ProductResponse productResponse;

    private Float price;
    private int numberOfProducts;
    private Float totalMoney;
    private String color;

}
