package com.hainguyen.shop.services;

import com.hainguyen.shop.dtos.request.OrderDto;
import com.hainguyen.shop.dtos.response.OrderResponse;
import com.hainguyen.shop.dtos.response.OrdersResponsePage;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface IOrderService {
    OrderResponse createOrder(OrderDto orderDto);

    OrderResponse getOrder(Long orderId);

    Boolean updateOrder(Long orderId, OrderDto orderDto);

    Boolean updateOrderStatusByVnpTxpRef(String vnpTxpRef, String status);

    Boolean deleteOrder(Long orderId);

    Boolean cancelOrder(Long orderId, Authentication authentication);

    List<OrderResponse> findByUserId(Long userId);

    OrdersResponsePage searchOrdersByKeyword(int pageNumber, int pageSize, String keyword);
}
