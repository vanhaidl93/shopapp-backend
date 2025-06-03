package com.hainguyen.shop.dtos.response;

import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse extends BaseResponse {

    private Long id;
    private String name;
    private Float price;
    private String thumbnail;
    private String description;
    private CategoryResponse categoryResponse;
    private List<ProductImageResponse> productImagesResponse;


}
