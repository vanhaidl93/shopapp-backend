package com.hainguyen.shop.dtos.request;

import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class OrderDetailDto {

    @Min(value = 1, message= "Order's ID must be >0")
    private Long orderId;
    @Min(value = 1, message = "Product's ID must be >0")
    private Long productId;
    @Min(value = 0, message = "Product's ID must be ≥ 0")
    private Float price;
    @Min(value = 1, message = "Number of Products must be ≥ 1")
    private int numberOfProducts;

    @Min(value = 0, message = "Total money must be ≥ 0")
    private Float totalMoney;

    private String color;

}
