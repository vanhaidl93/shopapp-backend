package com.hainguyen.shop.services.impl;

import com.hainguyen.shop.dtos.request.OrderDto;
import com.hainguyen.shop.dtos.response.OrderResponse;
import com.hainguyen.shop.dtos.response.OrdersResponsePage;
import com.hainguyen.shop.exceptions.ResourceNotFoundException;
import com.hainguyen.shop.mapper.OrderMapper;
import com.hainguyen.shop.models.*;
import com.hainguyen.shop.repositories.CouponRepo;
import com.hainguyen.shop.repositories.OrderDetailRepo;
import com.hainguyen.shop.repositories.OrderRepo;
import com.hainguyen.shop.repositories.UserRepo;
import com.hainguyen.shop.services.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final UserRepo userRepo;
    private final OrderRepo orderRepo;
    private final OrderDetailRepo orderDetailRepo;
    private final OrderMapper orderMapper;
    private final CouponRepo couponRepo;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderDto orderDto) {
        User existingUser = userRepo.findById(orderDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", orderDto.getUserId().toString()));

        Order newOrder = orderMapper.mapToOrder(orderDto,new Order());
        newOrder.setUser(existingUser);
        newOrder.setOrderDate(LocalDate.now());
        newOrder.setStatus(OrderStatus.PENDING);
        newOrder.setShippingDate(newOrder.getOrderDate().plusDays(3));
        newOrder.setActive(true);
        newOrder.setShippingAddress(orderDto.getShippingAddress());
        newOrder.setAddress(existingUser.getAddress());

        // Map CartItemsDto to OrderDetail and save database (include Order)
        List<OrderDetail> orderDetails =
                orderMapper.cartItemsMapToOrderDetail(orderDto.getCartItems());

        // maintain both side (order + order details) before save.
        orderDetails.forEach(newOrder::addOrderDetail);

        // save both side.
        Order savedOrder = orderRepo.save(newOrder);

        return orderMapper.mapToOrderResponse(savedOrder,new OrderResponse());
    }

    @Override
    public OrderResponse getOrder(Long id) {
        Order existingOrder = orderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id.toString()));

        return orderMapper.mapToOrderResponse(existingOrder,new OrderResponse());
    }

    @Override
    @Transactional
    public Boolean updateOrder(Long orderId, OrderDto orderDto) {

        Order existingOrder = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId.toString()));

        User existingUser = userRepo.findById(orderDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", orderDto.getUserId().toString()));

        Order updatedOrder = orderMapper.mapToOrder(orderDto, existingOrder);
        updatedOrder.setAddress(existingUser.getAddress());
        // Handle Coupon
        updatedOrder.setUser(existingUser);

        // Handle Coupon
        if (!orderDto.getCouponCode().trim().isEmpty()) {
            Coupon coupon = couponRepo.findByCode(orderDto.getCouponCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Coupon", "id", orderDto.getCouponCode()));
            updatedOrder.setCoupon(coupon);
        } else {
            updatedOrder.setCoupon(null); // or keep existing if you prefer
        }

        return true;
    }

    @Override
    public Boolean deleteOrder(Long id) {
        Order order = orderRepo.findById(id).orElse(null);
        // soft-delete
        if (order != null) {
            order.setActive(false);
            orderRepo.save(order);
            return true;
        }
        return false;
    }

    @Override
    public List<OrderResponse> findByUserId(Long userId) {

        return orderRepo.findByUserId(userId).stream()
                .map(order -> orderMapper.mapToOrderResponse(order, new OrderResponse()))
                .toList();
    }

    @Override
    public OrdersResponsePage searchOrdersByKeyword(int pageNumber, int pageSize, String keyword) {

        PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize, // page start at index: 0
                Sort.by("orderDate").descending());

        Page<Order> ordersPage =  orderRepo.searchOrdersByKeyword( keyword, pageRequest);
        int totalPages = ordersPage.getTotalPages();

        List<OrderResponse> ordersResponsesPage = ordersPage.getContent()
                .stream()
                .map(order -> orderMapper.mapToOrderResponse(order, new OrderResponse()))
                .toList();;

        return new OrdersResponsePage(ordersResponsesPage,pageNumber,totalPages);

    }


}
