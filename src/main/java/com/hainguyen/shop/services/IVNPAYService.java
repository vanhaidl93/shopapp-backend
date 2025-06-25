package com.hainguyen.shop.services;

import com.hainguyen.shop.dtos.request.PaymentDto;
import com.hainguyen.shop.dtos.request.PaymentQueryDto;
import com.hainguyen.shop.dtos.request.PaymentRefundDto;
import jakarta.servlet.http.HttpServletRequest;

public interface IVNPAYService {
    String createPaymentUrl(PaymentDto paymentDto, HttpServletRequest request);

    String queryTransaction(PaymentQueryDto paymentQueryDto, HttpServletRequest request);

    String refundTransaction(PaymentRefundDto paymentRefundDto);
}
