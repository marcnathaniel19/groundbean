package com.goodbean.api;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponse(
        String orderNumber,
        String createdAt,
        List<ReceiptItem> items,
        BigDecimal subtotal,
        BigDecimal bulkDiscount,
        BigDecimal idDiscount,
        BigDecimal rewardsDiscount,
        BigDecimal total,
        BigDecimal cashReceived,
        BigDecimal change,
        String paymentMethod,
        int pointsEarned,
        int pointsRedeemed,
        int rewardPointsBalance) {

    public record ReceiptItem(
            String productName,
            String size,
            String milk,
            int sweetness,
            int shots,
            int quantity,
            BigDecimal unitPrice,
            BigDecimal lineTotal) {
    }
}
