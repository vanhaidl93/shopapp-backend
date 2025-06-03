package com.hainguyen.shop.dtos.response;

import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductsResponsePage {
    private List<ProductResponse> productsResponse;
    private int currentPage;
    private int totalPages;
}
