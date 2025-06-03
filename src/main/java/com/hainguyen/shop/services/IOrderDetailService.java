package com.hainguyen.shop.services;

import com.hainguyen.shop.dtos.request.OrderDetailDto;
import com.hainguyen.shop.dtos.response.OrderDetailResponse;

import java.util.List;

public interface IOrderDetailService {
    void createOrderDetail(OrderDetailDto newOrderDetail);
    OrderDetailResponse getOrderDetail(Long id);
    Boolean updateOrderDetail(Long id, OrderDetailDto newOrderDetail);
    Boolean deleteById(Long id);
    List<OrderDetailResponse> findByOrderId(Long orderId);
}
