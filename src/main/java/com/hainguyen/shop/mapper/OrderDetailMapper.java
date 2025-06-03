package com.hainguyen.shop.mapper;

import com.hainguyen.shop.models.OrderDetail;
import com.hainguyen.shop.dtos.response.CategoryResponse;
import com.hainguyen.shop.dtos.response.OrderDetailResponse;
import com.hainguyen.shop.dtos.response.ProductImageResponse;
import com.hainguyen.shop.dtos.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderDetailMapper {
    private final ModelMapper modelMapper;

    public OrderDetailResponse mapToOrderDetailResponse(OrderDetail orderDetail,
                                                                   OrderDetailResponse orderDetailResponse) {

        orderDetailResponse = modelMapper.map(orderDetail, OrderDetailResponse.class);

        CategoryResponse categoryResponse = modelMapper.map(orderDetail.getProduct().getCategory(), CategoryResponse.class);
        List<ProductImageResponse> productImagesResponse = orderDetail.getProduct().getProductImages()
                .stream()
                .map(productImage -> modelMapper.map(productImage, ProductImageResponse.class))
                .toList();

        ProductResponse productResponse = modelMapper.map(orderDetail.getProduct(), ProductResponse.class);
        productResponse.setCategoryResponse(categoryResponse);
        productResponse.setProductImagesResponse(productImagesResponse);

        orderDetailResponse.setProductResponse(productResponse);

        return orderDetailResponse;
    }
}
