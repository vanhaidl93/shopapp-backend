package com.hainguyen.shop.dtos.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageResponse{

    private Long id;

    private Long productId;

    private String imageName;
}
