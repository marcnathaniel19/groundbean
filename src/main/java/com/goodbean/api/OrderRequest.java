package com.goodbean.api;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(
        List<OrderItemRequest> items,
        boolean studentStaffDiscount,
        boolean redeemRewards,
        int rewardPoints,
        String paymentMethod,
        BigDecimal cashReceived) {

    public record OrderItemRequest(
            String productName,
            String size,
            String milk,
            Integer sweetness,
            Integer shots,
            Integer quantity) {
    }
}
