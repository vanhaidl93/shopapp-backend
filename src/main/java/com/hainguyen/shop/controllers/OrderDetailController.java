package com.hainguyen.shop.controllers;

import com.hainguyen.shop.dtos.response.OrderDetailResponse;
import com.hainguyen.shop.services.IOrderDetailService;
import com.hainguyen.shop.utils.Constants;
import com.hainguyen.shop.dtos.request.OrderDetailDto;
import com.hainguyen.shop.dtos.response.SuccessResponse;
import com.hainguyen.shop.utils.LocalizationUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orderDetails")
@RequiredArgsConstructor
public class OrderDetailController {

    private final IOrderDetailService orderDetailService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("")
    public ResponseEntity<SuccessResponse> createOrderDetail(
            @Valid @RequestBody OrderDetailDto orderDetailDto) {

        orderDetailService.createOrderDetail(orderDetailDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessResponse(Constants.STATUS_201,
                        localizationUtils.getLocalizedMessage(Constants.MESSAGE_201)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(@PathVariable("id") Long id) {

        OrderDetailResponse orderDetailResponse = orderDetailService.getOrderDetail(id);

        return ResponseEntity.ok()
                .body(orderDetailResponse);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderDetailResponse>> getOrderDetails(
            @PathVariable("orderId") Long orderId) {

        List<OrderDetailResponse> orderDetailsResponse = orderDetailService.findByOrderId(orderId);

        return ResponseEntity.ok()
                .body(orderDetailsResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse> updateOrderDetail(@PathVariable("id") Long id,
                                                             @Valid @RequestBody OrderDetailDto orderDetailDTO ) {

        Boolean isUpdated = orderDetailService.updateOrderDetail(id, orderDetailDTO);

        return localizationUtils.getResponseChangeRecord(isUpdated,Constants.MESSAGE_417_UPDATE);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> deleteOrderDetail(@PathVariable("id") Long id) {

        Boolean isDeleted = orderDetailService.deleteById(id);

        return localizationUtils.getResponseChangeRecord(isDeleted,Constants.MESSAGE_417_DELETE);
    }
}
