package org.thanhpham.entity;

import com.google.api.client.util.Value;

public enum OrderStatus {
    @Value("PENDING")
    PENDING,
    @Value("CONFIRMED")
    CONFIRMED,
    @Value("SHIPPED")
    SHIPPED,
    @Value("DELIVERED")
    DELIVERED,
    @Value("CANCELLED")
    CANCELLED,
    @Value("FAILED_CANCEL")
    FAILED_CANCEL
}
