package com.hainguyen.shop.mapper;

import com.hainguyen.shop.dtos.request.CartItemDto;
import com.hainguyen.shop.dtos.request.OrderDto;
import com.hainguyen.shop.dtos.response.*;
import com.hainguyen.shop.exceptions.ResourceNotFoundException;
import com.hainguyen.shop.models.Order;
import com.hainguyen.shop.models.OrderDetail;
import com.hainguyen.shop.models.Product;
import com.hainguyen.shop.repositories.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final ModelMapper modelMapper;
    private final ProductRepo productRepo;

    public OrderResponse mapToOrderResponse(Order order, OrderResponse orderResponse) {
        orderResponse = modelMapper.map(order, OrderResponse.class);

        List<OrderDetailResponse> orderDetailsResponse = order.getOrderDetails().stream()
                .map(orderDetail -> {
                    OrderDetailResponse orderDetailResponse = modelMapper.map(orderDetail, OrderDetailResponse.class);

                    ProductResponse productResponse = modelMapper.map(orderDetail.getProduct(), ProductResponse.class);
                    CategoryResponse categoryResponse = modelMapper.map(orderDetail.getProduct().getCategory(), CategoryResponse.class);
                    List<ProductImageResponse> productImagesResponse =
                            orderDetail.getProduct().getProductImages().stream()
                                    .map(productImage -> modelMapper.map(productImage, ProductImageResponse.class))
                                    .toList();
                    productResponse.setCategoryResponse(categoryResponse);
                    productResponse.setProductImagesResponse(productImagesResponse);

                    orderDetailResponse.setProductResponse(productResponse);
                    return orderDetailResponse;
                })
                .toList();

        orderResponse.setOrderDetailsResponse(orderDetailsResponse);

        return orderResponse;
    }

    public List<OrderDetail> cartItemsMapToOrderDetail(List<CartItemDto> cartItemsDto, Order savedOrder) {

        return cartItemsDto
                .stream().map(item -> {
                    Product existingProduct = productRepo.findById(item.getProductId())
                            .orElseThrow(() -> new ResourceNotFoundException("Product",
                                    "id", item.getProductId().toString()));

                    return  OrderDetail.builder()
                            .order(savedOrder)
                            .product(existingProduct)
                            .price(existingProduct.getPrice())
                            .numberOfProducts(item.getQuantity())
                            .totalMoney(Math.round(existingProduct.getPrice() * item.getQuantity() * 100.0f) / 100.0f)
                            .build();
                })
                .toList();
    }


    public Order mapToOrder(OrderDto orderDto, Order order) {
        modelMapper.typeMap(OrderDto.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        modelMapper.map(orderDto, order);

        return order;
    }

}
