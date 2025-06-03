package com.hainguyen.shop.services.impl;

import com.hainguyen.shop.dtos.request.OrderDetailDto;
import com.hainguyen.shop.exceptions.ResourceNotFoundException;
import com.hainguyen.shop.mapper.OrderDetailMapper;
import com.hainguyen.shop.models.Order;
import com.hainguyen.shop.models.OrderDetail;
import com.hainguyen.shop.models.Product;
import com.hainguyen.shop.repositories.OrderDetailRepo;
import com.hainguyen.shop.repositories.OrderRepo;
import com.hainguyen.shop.repositories.ProductRepo;
import com.hainguyen.shop.dtos.response.OrderDetailResponse;
import com.hainguyen.shop.services.IOrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {

    private final OrderRepo orderRepo;
    private final OrderDetailRepo orderDetailRepo;
    private final ProductRepo productRepo;
    private final OrderDetailMapper orderDetailMapper;

    @Override
    public void createOrderDetail(OrderDetailDto orderDetailDto) {
        Product existingProduct = productRepo.findById(orderDetailDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", orderDetailDto.getProductId().toString()));

        Order existingOrder = orderRepo.findById(orderDetailDto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderDetailDto.getOrderId().toString()));

        OrderDetail orderDetail = OrderDetail.builder()
                .order(existingOrder)
                .product(existingProduct)
                .numberOfProducts(orderDetailDto.getNumberOfProducts())
                .price(orderDetailDto.getPrice())
                .totalMoney(orderDetailDto.getTotalMoney())
                .color(orderDetailDto.getColor())
                .build();
        orderDetailRepo.save(orderDetail);
    }

    @Override
    public OrderDetailResponse getOrderDetail(Long orderId) {
        OrderDetail existingOrderDetail = orderDetailRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetail", "id", orderId.toString()));

        return orderDetailMapper.mapToOrderDetailResponse(existingOrderDetail,new OrderDetailResponse());
    }

    @Override
    @Transactional
    public Boolean updateOrderDetail(Long id, OrderDetailDto newOrderDetail) {
        OrderDetail existingOrderDetail = orderDetailRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetail", "id", id.toString()));

        Order existingOrder = orderRepo.findById(newOrderDetail.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", newOrderDetail.getOrderId().toString()));

        Product existingProduct = productRepo.findById(newOrderDetail.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", newOrderDetail.getProductId().toString()));

        existingOrderDetail.setPrice(newOrderDetail.getPrice()); // price at the sale time.
        existingOrderDetail.setNumberOfProducts(newOrderDetail.getNumberOfProducts());
        existingOrderDetail.setTotalMoney(newOrderDetail.getTotalMoney());
        existingOrderDetail.setColor(newOrderDetail.getColor());
        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setProduct(existingProduct);

        return true;

    }

    @Override
    public Boolean deleteById(Long id) {
        orderDetailRepo.deleteById(id);

        return true;
    }

    @Override
    public List<OrderDetailResponse> findByOrderId(Long orderId) {
        return orderDetailRepo.findByOrderId(orderId).stream()
                .map(orderDetail ->
                        orderDetailMapper.mapToOrderDetailResponse(orderDetail,new OrderDetailResponse()))
                .toList();
    }
}
