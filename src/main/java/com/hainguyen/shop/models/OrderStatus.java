package com.hainguyen.shop.models;

public class OrderStatus {
    private OrderStatus() {
    }

    public static final String PENDING = "pending";
    public static final String PROCESSING = "processing";
    public static final String SHIPPED = "shipped";
    public static final String DELIVERED = "delivered";
    public static final String CANCELLED = "cancelled";
}
