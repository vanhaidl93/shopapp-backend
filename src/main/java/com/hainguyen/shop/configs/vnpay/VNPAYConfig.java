package com.hainguyen.shop.configs.vnpay;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class VNPAYConfig {

    @Value("${vnpay.payment-url}")
    private String vnpPayUrl;

    @Value("${vnpay.transaction-url}")
    private String vnpApiUrl;

    @Value("${vnpay.return-payment-url}")
    private String vnpReturnUrl;

    // the merchant will register, the below info will be sent to their email.
    @Value("${vnpay.tmn-code}")
    private String vnpTmnCode;

    @Value("${vnpay.secret-key}")
    private String secretKey;

}
