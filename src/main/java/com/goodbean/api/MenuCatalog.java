package com.goodbean.api;

import java.util.List;

public final class MenuCatalog {
    private static final List<Product> PRODUCTS = List.of(
            new Product("Americano", "Espresso", 90, "Rich espresso, softened with hot water.", "images/americano.jpg", "Freshly brewed coffee in a ceramic cup.", false, true),
            new Product("Latte", "Espresso", 120, "Smooth espresso with silky steamed milk.", "images/latte.jpg", "A latte with freshly poured milk foam.", true, true),
            new Product("Cappuccino", "Espresso", 120, "A cozy balance of espresso and foam.", "images/cappuccino.jpg", "Cappuccino served in a cup.", true, true),
            new Product("Green Tea", "Tea", 80, "Light, refreshing, and gently soothing.", "images/green-tea.jpg", "A cup of freshly steeped tea.", false, false),
            new Product("Milk Tea", "Tea", 100, "Creamy black tea with a little sweetness.", "images/milk-tea.jpg", "A chilled tea drink ready to enjoy.", true, false),
            new Product("Chai Latte", "Tea", 110, "Warm spices blended with steamed milk.", "images/chai-latte.jpg", "A warm milk tea served in a cup.", true, false),
            new Product("Mocha Frappe", "Frappuccino", 150, "A chocolatey, icy coffee treat.", "images/mocha-frappe.jpg", "A creamy iced coffee drink.", true, true),
            new Product("Caramel Frappe", "Frappuccino", 150, "Blended coffee with buttery caramel.", "images/caramel-frappe.jpg", "A chilled coffee drink served over ice.", true, true),
            new Product("Cookies & Cream Frappe", "Frappuccino", 160, "A creamy blend with cookie goodness.", "images/cookies-cream-frappe.jpg", "A rich, creamy blended drink.", true, true)
    );

    private MenuCatalog() {
    }

    public static List<Product> all() {
        return List.copyOf(PRODUCTS.values());
    }

    public static Product find(String name) {
        return name == null ? null : PRODUCTS.stream()
                .filter(product -> product.name().equals(name))
                .findFirst()
                .orElse(null);
    }

    public record Product(
            String name,
            String category,
            int basePrice,
            String description,
            String image,
            String imageAlt,
            boolean allowsMilk,
            boolean allowsShots) {
    }
}
