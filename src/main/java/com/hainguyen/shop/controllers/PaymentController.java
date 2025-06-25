package com.hainguyen.shop.controllers;

import com.hainguyen.shop.dtos.request.PaymentDto;
import com.hainguyen.shop.dtos.request.PaymentQueryDto;
import com.hainguyen.shop.dtos.request.PaymentRefundDto;
import com.hainguyen.shop.dtos.response.PaymentResponse;
import com.hainguyen.shop.services.IVNPAYService;
import com.hainguyen.shop.utils.Constants;
import com.hainguyen.shop.utils.LocalizationUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/payments")
public class PaymentController {

    private final IVNPAYService vnPayService;
    private final LocalizationUtils localizationUtils;


    @PostMapping("/createPaymentUrl")
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentDto paymentDto,
                                                         HttpServletRequest request) {

        String paymentUrl = vnPayService.createPaymentUrl(paymentDto, request);

        return ResponseEntity.ok()
                .body(PaymentResponse.builder()
                        .status(HttpStatus.OK)
                        .message(localizationUtils.getLocalizedMessage(Constants.MESSAGE_200))
                        .data(paymentUrl)
                        .build());
    }

    @PostMapping("/query")
    public ResponseEntity<PaymentResponse> queryTransaction(@RequestBody PaymentQueryDto paymentQueryDTO,
                                                            HttpServletRequest request) {

        String result = vnPayService.queryTransaction(paymentQueryDTO, request);
        return ResponseEntity.ok()
                .body(PaymentResponse.builder()
                        .status(HttpStatus.OK)
                        .message(localizationUtils.getLocalizedMessage(Constants.MESSAGE_200))
                        .data(result)
                        .build());
    }

    @PostMapping("/refund")
    public ResponseEntity<PaymentResponse> refundTransaction(@RequestBody PaymentRefundDto paymentRefundDTO) {

        String response = vnPayService.refundTransaction(paymentRefundDTO);
        return ResponseEntity.ok()
                .body(PaymentResponse.builder()
                        .message(localizationUtils.getLocalizedMessage(Constants.MESSAGE_200))
                        .status(HttpStatus.OK)
                        .data(response)
                        .build());
    }

}