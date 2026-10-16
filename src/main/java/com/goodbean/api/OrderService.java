package com.goodbean.api;

import com.goodbean.api.OrderRequest.OrderItemRequest;
import com.goodbean.api.OrderResponse.ReceiptItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class OrderService {
    private static final BigDecimal ZERO = new BigDecimal("0.00");
    private static final BigDecimal REWARD_VALUE = new BigDecimal("50.00");
    private static final int MAX_DRINKS = 50;

    public OrderResponse createOrder(OrderRequest request) {
        if (request == null || request.items() == null || request.items().isEmpty()) {
            throw new IllegalArgumentException("Add at least one drink before placing your order.");
        }
        if (request.items().size() > MAX_DRINKS) {
            throw new IllegalArgumentException("An order can contain no more than 50 line items.");
        }
        if (request.rewardPoints() < 0 || request.rewardPoints() > 1_000_000) {
            throw new IllegalArgumentException("The rewards balance is invalid.");
        }
        String paymentMethod = requirePaymentMethod(request.paymentMethod());

        List<ReceiptItem> receiptItems = new ArrayList<>();
        BigDecimal subtotal = ZERO;
        int drinkCount = 0;
        for (OrderItemRequest item : request.items()) {
            validateItem(item);
            drinkCount += item.quantity();
            if (drinkCount > MAX_DRINKS) {
                throw new IllegalArgumentException("An order can contain no more than 50 drinks.");
            }
            MenuCatalog.Product product = MenuCatalog.find(item.productName());
            BigDecimal unitPrice = price(product, item);
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(item.quantity())).setScale(2, RoundingMode.HALF_UP);
            subtotal = subtotal.add(lineTotal);
            receiptItems.add(new ReceiptItem(
                    product.name(), item.size(), item.milk(), item.sweetness(), item.shots(),
                    item.quantity(), unitPrice, lineTotal));
        }

        BigDecimal bulkDiscount = drinkCount >= 3 ? subtotal.multiply(new BigDecimal("0.10")).setScale(2, RoundingMode.HALF_UP) : ZERO;
        BigDecimal idDiscount = request.studentStaffDiscount()
                ? subtotal.multiply(new BigDecimal("0.05")).setScale(2, RoundingMode.HALF_UP) : ZERO;
        BigDecimal beforeRewards = subtotal.subtract(bulkDiscount).subtract(idDiscount);
        if (request.redeemRewards() && (request.rewardPoints() < 100 || beforeRewards.compareTo(REWARD_VALUE) < 0)) {
            throw new IllegalArgumentException("You need 100 points and at least ₱50 due after other discounts to redeem a reward.");
        }
        BigDecimal rewardsDiscount = request.redeemRewards() ? REWARD_VALUE.min(beforeRewards) : ZERO;
        BigDecimal total = beforeRewards.subtract(rewardsDiscount).setScale(2, RoundingMode.HALF_UP);

        BigDecimal cashReceived = null;
        BigDecimal change = ZERO;
        if ("Cash".equals(paymentMethod)) {
            cashReceived = request.cashReceived();
            if (cashReceived == null || cashReceived.signum() < 0) {
                throw new IllegalArgumentException("Enter a valid, non-negative cash amount.");
            }
            cashReceived = cashReceived.setScale(2, RoundingMode.HALF_UP);
            if (cashReceived.compareTo(total) < 0) {
                throw new IllegalArgumentException("Cash received is less than the order total of ₱" + total.toPlainString() + ".");
            }
            change = cashReceived.subtract(total).setScale(2, RoundingMode.HALF_UP);
        }

        int pointsRedeemed = request.redeemRewards() ? 100 : 0;
        int pointsEarned = total.divideToIntegralValue(BigDecimal.TEN).intValueExact();
        int newRewardBalance = request.rewardPoints() - pointsRedeemed + pointsEarned;
        String orderNumber = "GB-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT);

        return new OrderResponse(
                orderNumber, Instant.now().toString(), List.copyOf(receiptItems), subtotal,
                bulkDiscount, idDiscount, rewardsDiscount, total, cashReceived, change,
                paymentMethod, pointsEarned, pointsRedeemed, newRewardBalance);
    }

    private static void validateItem(OrderItemRequest item) {
        if (item == null) {
            throw new IllegalArgumentException("An order item is invalid.");
        }
        MenuCatalog.Product product = MenuCatalog.find(item.productName());
        if (product == null) {
            throw new IllegalArgumentException("Unknown drink: " + item.productName());
        }
        if (item.size() == null || !List.of("Small", "Medium", "Large").contains(item.size())) {
            throw new IllegalArgumentException("Choose a valid size for " + product.name() + ".");
        }
        if (item.quantity() == null || item.quantity() < 1 || item.quantity() > MAX_DRINKS) {
            throw new IllegalArgumentException("Each drink quantity must be from 1 to 50.");
        }
        if (item.sweetness() == null || !List.of(0, 25, 50, 75, 100).contains(item.sweetness())) {
            throw new IllegalArgumentException("Choose a valid sweetness level for " + product.name() + ".");
        }
        if (item.shots() == null || item.shots() < 0 || item.shots() > 3 || (!product.allowsShots() && item.shots() != 0)) {
            throw new IllegalArgumentException("The extra-shot selection is invalid for " + product.name() + ".");
        }
        if (product.allowsMilk()) {
            if (item.milk() == null || !List.of("Whole milk", "Oat milk", "Almond milk").contains(item.milk())) {
                throw new IllegalArgumentException("Choose a valid milk for " + product.name() + ".");
            }
        } else if (!"No milk".equals(item.milk())) {
            throw new IllegalArgumentException(product.name() + " should not include milk.");
        }
    }

    private static BigDecimal price(MenuCatalog.Product product, OrderItemRequest item) {
        int price = product.basePrice();
        price += switch (item.size()) {
            case "Medium" -> 20;
            case "Large" -> 40;
            default -> 0;
        };
        if ("Oat milk".equals(item.milk()) || "Almond milk".equals(item.milk())) {
            price += 30;
        }
        price += item.shots() * 25;
        return BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP);
    }

    private static String requirePaymentMethod(String method) {
        if (method == null || !List.of("Cash", "GCash", "Card").contains(method)) {
            throw new IllegalArgumentException("Choose Cash, GCash, or Card as your payment method.");
        }
        return method;
    }
}
