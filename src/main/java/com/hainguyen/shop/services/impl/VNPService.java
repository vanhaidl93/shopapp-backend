package com.hainguyen.shop.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.hainguyen.shop.configs.vnpay.VNPAYConfig;
import com.hainguyen.shop.dtos.request.PaymentDto;
import com.hainguyen.shop.dtos.request.PaymentQueryDto;
import com.hainguyen.shop.dtos.request.PaymentRefundDto;
import com.hainguyen.shop.services.IVNPAYService;
import com.hainguyen.shop.utils.VNPAYUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VNPService implements IVNPAYService {

    private final VNPAYConfig vnpayConfig;
    private final VNPAYUtils vnpayUtils;


    @Override
    public String createPaymentUrl(PaymentDto paymentDto, HttpServletRequest request) {
        /*
        required urlPayment: ( using source code demo to create - refactor from "Servlet Code")

        * https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?vnp_Amount=1806000
        * &vnp_Command=pay
        * &vnp_CreateDate=20210801153333
        * &vnp_CurrCode=VND
        * &vnp_IpAddr=127.0.0.1
        * &vnp_Locale=vn
        * &vnp_OrderInfo=Thanh+toan+don+hang+%3A5
        * &vnp_OrderType=other
        * &vnp_ReturnUrl=https%3A%2F%2Fdomainmerchant.vn%2FReturnUrl
        * &vnp_TmnCode=DEMOV210
        * &vnp_TxnRef=5
        * &vnp_Version=2.1.0
        * &vnp_SecureHash=3e0d61a0c0534b2e36680b3f7277743e8784cc4e1d68fa7d276....
        * */

        String version = "2.1.0";
        String command = "pay";
        String orderType = "other";
        long amount = paymentDto.amount() * 100; // amount need multiply 100
        String bankCode = paymentDto.bankCode(); // optional (NCB - Testing environment)

        String transactionReference = vnpayUtils.getRandomNumber(8);
        String clientIpAddress = vnpayUtils.getIpAddress(request);
        String terminalCode = vnpayConfig.getVnpTmnCode();

        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", version);
        params.put("vnp_Command", command);
        params.put("vnp_TmnCode", terminalCode);
        params.put("vnp_Amount", String.valueOf(amount));
        params.put("vnp_CurrCode", "VND");

        if (bankCode != null && !bankCode.isEmpty()) {
            params.put("vnp_BankCode", bankCode);
        }
        params.put("vnp_TxnRef", transactionReference);
        params.put("vnp_OrderInfo", "Thanh toan don hang:" + transactionReference);
        params.put("vnp_OrderType", orderType);

        String locale = paymentDto.language();
        if (locale != null && !locale.isEmpty()) {
            params.put("vnp_Locale", locale);
        } else {
            params.put("vnp_Locale", "vn");
        }

        params.put("vnp_ReturnUrl", vnpayConfig.getVnpReturnUrl());
        params.put("vnp_IpAddr", clientIpAddress);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String createdDate = dateFormat.format(calendar.getTime());
        params.put("vnp_CreateDate", createdDate);

        calendar.add(Calendar.MINUTE, 15);
        String expirationDate = dateFormat.format(calendar.getTime());
        params.put("vnp_ExpireDate", expirationDate);

        List<String> sortedFieldNames = new ArrayList<>(params.keySet());
        Collections.sort(sortedFieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder queryData = new StringBuilder();

        for (Iterator<String> iterator = sortedFieldNames.iterator(); iterator.hasNext();) {
            String fieldName = iterator.next();
            // do something with field. create parameter:....?key=value&....
            String fieldValue = params.get(fieldName);

            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName)
                        .append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                queryData.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII))
                        .append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (iterator.hasNext()) {
                    hashData.append('&');
                    queryData.append('&');
                }
            }
        }

        String secureHash = vnpayUtils.hmacSHA512(vnpayConfig.getSecretKey(), hashData.toString());
        queryData.append("&vnp_SecureHash=").append(secureHash);

        return vnpayConfig.getVnpPayUrl() + "?" + queryData;
    }

    @Override
    public String queryTransaction(PaymentQueryDto paymentQueryDTO, HttpServletRequest request) {
        // repairing parameters for VNPay
        String requestId = vnpayUtils.getRandomNumber(8);
        String version = "2.1.0";
        String command = "querydr";
        String terminalCode = vnpayConfig.getVnpTmnCode();
        String transactionReference = paymentQueryDTO.orderId().toString();
        String transactionDate = paymentQueryDTO.transactionDate().toString();
        String createDate = vnpayUtils.getCurrentDateTime();
        String clientIpAddress = vnpayUtils.getIpAddress(request);

        Map<String, String> params = new HashMap<>();
        params.put("vnp_RequestId", requestId);
        params.put("vnp_Version", version);
        params.put("vnp_Command", command);
        params.put("vnp_TmnCode", terminalCode);
        params.put("vnp_TxnRef", transactionReference);
        params.put("vnp_OrderInfo", "Check transaction result for OrderId:" + transactionReference);
        params.put("vnp_TransactionDate", transactionDate);
        params.put("vnp_CreateDate", createDate);
        params.put("vnp_IpAddr", clientIpAddress);

        // create hash and secretKey for security.
        String hashData = String.join("|", requestId, version, command,
                terminalCode, transactionReference, transactionDate, createDate, clientIpAddress, "Check transaction");
        String secureHash = vnpayUtils.hmacSHA512(vnpayConfig.getSecretKey(), hashData);
        params.put("vnp_SecureHash", secureHash);

        // send a request API to VNPay
        HttpURLConnection connection = null;
        try {
            URL apiUrl = new URL(vnpayConfig.getVnpApiUrl());

            connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (DataOutputStream writer = new DataOutputStream(connection.getOutputStream())) {
            writer.writeBytes(new Gson().toJson(params));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int responseCode = 0;
        try {
            responseCode = connection.getResponseCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            if (responseCode == 200) {
                return response.toString();
            } else {
                throw new RuntimeException("VNPay API Error: " + response.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String refundTransaction(PaymentRefundDto refundDto) {
        String requestId = vnpayUtils.getRandomNumber(8); // Unique request ID
        String version = "2.1.0"; // API version
        String command = "refund"; // Refund command
        String terminalCode = vnpayConfig.getVnpTmnCode(); // Terminal code

        // Build VNPay parameters
        Map<String, String> params = new LinkedHashMap<>();
        params.put("vnp_RequestId", requestId);
        params.put("vnp_Version", version);
        params.put("vnp_Command", command);
        params.put("vnp_TmnCode", terminalCode);
        params.put("vnp_TransactionType", refundDto.transactionType());
        params.put("vnp_TxnRef", refundDto.orderId().toString());
        params.put("vnp_Amount", String.valueOf(refundDto.amount() * 100)); // Amount in the smallest currency unit
        params.put("vnp_OrderInfo", "Refund for OrderId: " + refundDto.orderId());
        params.put("vnp_TransactionDate", refundDto.transactionDate().toString());
        params.put("vnp_CreateBy", refundDto.createBy().toString());
        params.put("vnp_IpAddr", refundDto.ipAddress());

        // Generate secure hash
        String hashData = String.join("|",
                requestId,
                version,
                command,
                terminalCode,
                refundDto.transactionType(),
                refundDto.orderId().toString(),
                String.valueOf(refundDto.amount() * 100),
                refundDto.transactionDate().toString(),
                refundDto.createBy().toString(),
                refundDto.ipAddress(),
                "Refund for OrderId: " + refundDto.orderId());
        String secureHash = vnpayUtils.hmacSHA512(vnpayConfig.getSecretKey(), hashData);
        params.put("vnp_SecureHash", secureHash);

        // Send request to VNPay
        HttpURLConnection connection;
        try {
            URL apiUrl = new URL(vnpayConfig.getVnpApiUrl());
            connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("POST");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream outputStream = connection.getOutputStream()) {
            byte[] jsonPayload = new ObjectMapper().writeValueAsBytes(params);
            outputStream.write(jsonPayload, 0, jsonPayload.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Read response
        int responseCode = 0;
        try {
            responseCode = connection.getResponseCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Failed to process refund. Response code: " + responseCode);
        }

        try (BufferedReader bufferedReader =
                     new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                responseBuilder.append(line.trim());
            }
            return responseBuilder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
