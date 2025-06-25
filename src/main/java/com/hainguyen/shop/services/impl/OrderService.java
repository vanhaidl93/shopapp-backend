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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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

        Order newOrder = orderMapper.mapToOrder(orderDto, new Order());
        newOrder.setUser(existingUser);
        newOrder.setOrderDate(LocalDate.now());
        newOrder.setStatus(OrderStatus.PENDING);
        newOrder.setShippingDate(newOrder.getOrderDate().plusDays(3));
        newOrder.setActive(true);
        newOrder.setShippingAddress(orderDto.getShippingAddress());
        newOrder.setAddress(existingUser.getAddress());
        newOrder.setVnpTxnRef(orderDto.getVnpTxnRef());

        // Map CartItemsDto to OrderDetail and save database (include Order)
        List<OrderDetail> orderDetails = orderMapper.cartItemsMapToOrderDetail(orderDto.getCartItems());

        // maintain both side (order + order details) before save.
        orderDetails.forEach(newOrder::addOrderDetail);

        if(!orderDto.getCouponCode().trim().isEmpty()){
            Coupon existingCoupon = couponRepo.findByCode(orderDto.getCouponCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Coupon","couponCode",orderDto.getCouponCode()));
            newOrder.setCoupon(existingCoupon);
        }else {
            newOrder.setCoupon(null);
        }

        // save both side.
        Order savedOrder = orderRepo.save(newOrder);

        return orderMapper.mapToOrderResponse(savedOrder, new OrderResponse());
    }

    @Override
    public OrderResponse getOrder(Long id) {
        Order existingOrder = orderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id.toString()));

        return orderMapper.mapToOrderResponse(existingOrder, new OrderResponse());
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
    @Transactional
    public Boolean updateOrderStatusByVnpTxpRef(String vnpTxpRef, String status) {

        Order existingOrder = orderRepo.findByVnpTxnRef(vnpTxpRef)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "vnpTxpRef",vnpTxpRef));
        existingOrder.setStatus(status);

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
    @Transactional
    public Boolean cancelOrder(Long orderId, Authentication authentication) {

        Order existingOrder = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", orderId.toString()));

        User authenticatedUser = userRepo.findByPhoneNumber(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User", "phoneNumber", authentication.getName()));


        if (!Objects.equals(authenticatedUser.getId(), existingOrder.getUser().getId())) {
            throw new IllegalArgumentException("You do not have permission to cancel this order");
        }

        if (existingOrder.getStatus().equals(OrderStatus.DELIVERED)
                || existingOrder.getStatus().equals(OrderStatus.SHIPPED)
                || existingOrder.getStatus().equals(OrderStatus.PROCESSING)) {
            throw new IllegalArgumentException("You cannot cancel an order with status: " + existingOrder.getStatus());
        }

        existingOrder.setStatus(OrderStatus.CANCELLED);
        existingOrder.setActive(false);
        return true;
    }

    @Override
    public List<OrderResponse> findByUserId(Long userId) {

        return orderRepo.findByUserId(userId).stream()
                .map(order -> orderMapper.mapToOrderResponse(order, new OrderResponse()))
                .toList();
    }

    @Override
    public OrdersResponsePage searchOrdersByKeyword(int pageNumber, int pageSize, String keyword) {

        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, // page start at index: 0
                Sort.by("orderDate").descending());

        Page<Order> ordersPage = orderRepo.searchOrdersByKeyword(keyword, pageRequest);
        int totalPages = ordersPage.getTotalPages();

        List<OrderResponse> ordersResponsesPage = ordersPage.getContent()
                .stream()
                .map(order -> orderMapper.mapToOrderResponse(order, new OrderResponse()))
                .toList();
        ;

        return new OrdersResponsePage(ordersResponsesPage, pageNumber, totalPages);

    }


}
