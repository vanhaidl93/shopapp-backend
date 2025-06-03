package com.hainguyen.shop.services;

import com.hainguyen.shop.dtos.request.OrderDto;
import com.hainguyen.shop.dtos.response.OrderResponse;
import com.hainguyen.shop.dtos.response.OrdersResponsePage;

import java.util.List;

public interface IOrderService {
    OrderResponse createOrder(OrderDto orderDto);

    OrderResponse getOrder(Long id);

    Boolean updateOrder(Long id, OrderDto orderDto);

    Boolean deleteOrder(Long id);

    List<OrderResponse> findByUserId(Long userId);

    OrdersResponsePage searchOrdersByKeyword(int pageNumber, int pageSize, String keyword);
}
