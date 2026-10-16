package com.goodbean.api;

import com.goodbean.api.OrderRequest.OrderItemRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderServiceTest {
    private final OrderService orderService = new OrderService();

    @Test
    void calculatesAuthoritativePricesDiscountsAndRewards() {
        OrderRequest request = new OrderRequest(
                List.of(
                        new OrderItemRequest("Latte", "Small", "Whole milk", 50, 0, 2),
                        new OrderItemRequest("Americano", "Small", "No milk", 50, 0, 1)),
                true, false, 0, "GCash", null);

        OrderResponse response = orderService.createOrder(request);

        assertEquals(new BigDecimal("330.00"), response.subtotal());
        assertEquals(new BigDecimal("33.00"), response.bulkDiscount());
        assertEquals(new BigDecimal("16.50"), response.idDiscount());
        assertEquals(new BigDecimal("280.50"), response.total());
        assertEquals(28, response.pointsEarned());
        assertEquals(28, response.rewardPointsBalance());
    }

    @Test
    void redeemsRewardsAndCalculatesCashChange() {
        OrderRequest request = new OrderRequest(
                List.of(new OrderItemRequest("Mocha Frappe", "Small", "Whole milk", 50, 0, 1)),
                false, true, 100, "Cash", new BigDecimal("120.00"));

        OrderResponse response = orderService.createOrder(request);

        assertEquals(new BigDecimal("100.00"), response.total());
        assertEquals(new BigDecimal("20.00"), response.change());
        assertEquals(100, response.pointsRedeemed());
        assertEquals(10, response.rewardPointsBalance());
    }

    @Test
    void rejectsCashBelowServerCalculatedTotal() {
        OrderRequest request = new OrderRequest(
                List.of(new OrderItemRequest("Latte", "Small", "Whole milk", 50, 0, 1)),
                false, false, 0, "Cash", new BigDecimal("1.00"));

        assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(request));
    }

    @Test
    void rejectsInvalidCustomizationInsteadOfTrustingClientPrices() {
        OrderRequest request = new OrderRequest(
                List.of(new OrderItemRequest("Green Tea", "Small", "Oat milk", 50, 0, 1)),
                false, false, 0, "Card", null);

        assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(request));
    }
}
