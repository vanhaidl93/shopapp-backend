package com.hainguyen.shop.controllers;

import com.hainguyen.shop.dtos.request.OrderDto;
import com.hainguyen.shop.dtos.response.OrderResponse;
import com.hainguyen.shop.dtos.response.OrdersResponsePage;
import com.hainguyen.shop.dtos.response.SuccessResponse;
import com.hainguyen.shop.services.IOrderService;
import com.hainguyen.shop.utils.Constants;
import com.hainguyen.shop.utils.LocalizationUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final IOrderService orderService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderDto orderDto) {

        OrderResponse saveOrder = orderService.createOrder(orderDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(saveOrder);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(@PathVariable("userId") Long userId) {

        List<OrderResponse> ordersResponse = orderService.findByUserId(userId);

        return ResponseEntity.ok(ordersResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder( @PathVariable("id") Long orderId) {
        OrderResponse existingOrderResponse = orderService.getOrder(orderId);

        return ResponseEntity.ok(existingOrderResponse);
    }

    @GetMapping("/search-keyword")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<OrdersResponsePage> searchOrdersByKeyword(@RequestParam(defaultValue = "1") int pageNumber,
                                                            @RequestParam(defaultValue = "10") int pageSize,
                                                            @RequestParam(defaultValue = "") String keyword) {

        OrdersResponsePage ordersPerPage =
                orderService.searchOrdersByKeyword(pageNumber, pageSize, keyword);
        return ResponseEntity.ok()
                .body(ordersPerPage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse> updateOrder(@PathVariable long id,
                                                       @Valid @RequestBody OrderDto orderDto) {

        Boolean isUpdated = orderService.updateOrder(id, orderDto);

        return localizationUtils.getResponseChangeRecord(isUpdated,Constants.MESSAGE_417_UPDATE);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> deleteOrder(@PathVariable Long id) {

        //soft-delete
        Boolean isDeleted = orderService.deleteOrder(id);

        return localizationUtils.getResponseChangeRecord(isDeleted,Constants.MESSAGE_417_DELETE);
    }

    @PutMapping("/{vnpTxpRef}/status")
    public ResponseEntity<SuccessResponse> updateOrderStatus(@PathVariable String vnpTxpRef,
                                                             @RequestParam String status){

        boolean isUpdated = orderService.updateOrderStatusByVnpTxpRef(vnpTxpRef, status);

        return localizationUtils.getResponseChangeRecord(isUpdated,Constants.MESSAGE_417_UPDATE);
    }

    @PutMapping("/cancel/{orderId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<SuccessResponse> cancelOrder(@PathVariable long orderId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean cancelOrder = orderService.cancelOrder(orderId,authentication);

        return ResponseEntity.ok()
                .body(new SuccessResponse(Constants.STATUS_200, localizationUtils.getLocalizedMessage(Constants.MESSAGE_200)));
    }

}
