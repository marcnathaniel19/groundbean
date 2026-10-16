import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Smart Automated Coffee Ordering Kiosk
 * Demonstrates: switch (menu routing), if-else (validation / pricing),
 * while + do-while loops (session and multi-order), for loop (brewing progress bar).
 */
public class CoffeeKiosk {

    static final Scanner sc = new Scanner(System.in);

    // Inventory (limited stock for milk alternatives)
    static int oatMilkStock = 5;
    static int almondMilkStock = 5;

    // Current order
    static final List<String> orderNames = new ArrayList<>();
    static final List<Double> orderPrices = new ArrayList<>();

    public static void main(String[] args) {
        boolean kioskRunning = true;

        // while loop: kiosk keeps serving customers until staff shuts it down
        while (kioskRunning) {
            orderNames.clear();
            orderPrices.clear();

            printBanner();
            System.out.println("1. Start a new order");
            System.out.println("0. Shut down kiosk (staff only)");
            int start = readInt("Choose: ", 0, 1);

            if (start == 0) {
                kioskRunning = false;
                continue;
            }

            // do-while loop: order multiple drinks in one session
            boolean addMore;
            do {
                orderOneDrink();
                addMore = askYesNo("\nAdd another drink? (y/n): ");
            } while (addMore);

            if (!orderNames.isEmpty()) {
                checkoutAndBrew();
            }
        }
        System.out.println("\nKiosk shutting down. Goodbye!");
    }

    // ---------- ORDERING ----------

    static void orderOneDrink() {
        System.out.println("\n=== SELECT CATEGORY ===");
        System.out.println("1. Espresso");
        System.out.println("2. Tea");
        System.out.println("3. Frappuccino");
        int category = readInt("Choose category: ", 1, 3);

        String drink = "";
        double price = 0;
        boolean usesMilk = true;
        boolean allowShots = true;

        // switch: route the customer to the right drink list
        switch (category) {
            case 1:
                System.out.println("\n-- Espresso --");
                System.out.println("1. Americano   PHP 90");
                System.out.println("2. Latte       PHP 120");
                System.out.println("3. Cappuccino  PHP 120");
                switch (readInt("Choose drink: ", 1, 3)) {
                    case 1: drink = "Americano";  price = 90;  usesMilk = false; break;
                    case 2: drink = "Latte";      price = 120; break;
                    case 3: drink = "Cappuccino"; price = 120; break;
                }
                break;
            case 2:
                System.out.println("\n-- Tea --");
                System.out.println("1. Green Tea   PHP 80");
                System.out.println("2. Milk Tea    PHP 100");
                System.out.println("3. Chai Latte  PHP 110");
                allowShots = false;
                switch (readInt("Choose drink: ", 1, 3)) {
                    case 1: drink = "Green Tea";  price = 80;  usesMilk = false; break;
                    case 2: drink = "Milk Tea";   price = 100; break;
                    case 3: drink = "Chai Latte"; price = 110; break;
                }
                break;
            case 3:
                System.out.println("\n-- Frappuccino --");
                System.out.println("1. Mocha Frappe          PHP 150");
                System.out.println("2. Caramel Frappe        PHP 150");
                System.out.println("3. Cookies & Cream Frappe PHP 160");
                switch (readInt("Choose drink: ", 1, 3)) {
                    case 1: drink = "Mocha Frappe";           price = 150; break;
                    case 2: drink = "Caramel Frappe";         price = 150; break;
                    case 3: drink = "Cookies & Cream Frappe"; price = 160; break;
                }
                break;
            default:
                System.out.println("Invalid category.");
                return;
        }

        // Size (upcharge)
        System.out.println("\nSize: 1. Small (+0)  2. Medium (+20)  3. Large (+40)");
        int size = readInt("Choose size: ", 1, 3);
        String sizeName;
        if (size == 1) {
            sizeName = "Small";
        } else if (size == 2) {
            sizeName = "Medium";
            price += 20;
        } else {
            sizeName = "Large";
            price += 40;
        }

        // Milk choice with inventory check and upcharge
        String milk = "No milk";
        if (usesMilk) {
            boolean milkChosen = false;
            while (!milkChosen) {
                System.out.println("\nMilk: 1. Whole (+0)  2. Oat (+30)  3. Almond (+30)");
                int m = readInt("Choose milk: ", 1, 3);
                if (m == 1) {
                    milk = "Whole milk";
                    milkChosen = true;
                } else if (m == 2) {
                    if (oatMilkStock > 0) {
                        oatMilkStock--;
                        milk = "Oat milk";
                        price += 30;
                        milkChosen = true;
                    } else {
                        System.out.println("Sorry, oat milk is out of stock. Please pick another.");
                    }
                } else {
                    if (almondMilkStock > 0) {
                        almondMilkStock--;
                        milk = "Almond milk";
                        price += 30;
                        milkChosen = true;
                    } else {
                        System.out.println("Sorry, almond milk is out of stock. Please pick another.");
                    }
                }
            }
        }

        // Sweetness
        System.out.println("\nSweetness: 1. 0%  2. 25%  3. 50%  4. 75%  5. 100%");
        int sw = readInt("Choose sweetness: ", 1, 5);
        int sweetness = (sw - 1) * 25;

        // Extra shots (max 3, PHP 25 each)
        int shots = 0;
        if (allowShots) {
            shots = readInt("\nExtra espresso shots (0-3, +25 each): ", 0, 3);
            price += shots * 25;
        }

        String summary = sizeName + " " + drink + " | " + milk + " | " + sweetness + "% sweet"
                + (shots > 0 ? " | +" + shots + " shot(s)" : "");
        orderNames.add(summary);
        orderPrices.add(price);

        System.out.printf("%nAdded: %s  -> PHP %.2f%n", summary, price);
    }

    // ---------- CHECKOUT ----------

    static void checkoutAndBrew() {
        System.out.println("\n=========== YOUR ORDER ===========");
        double subtotal = 0;
        for (int i = 0; i < orderNames.size(); i++) {
            System.out.printf("%d. %s  PHP %.2f%n", i + 1, orderNames.get(i), orderPrices.get(i));
            subtotal += orderPrices.get(i);
        }

        // Discounts
        double discount = 0;
        if (orderNames.size() >= 3) {
            discount += subtotal * 0.10;
            System.out.println("Bulk discount (3+ drinks): 10% off");
        }
        if (askYesNo("Do you have a valid student/staff ID? (y/n): ")) {
            discount += subtotal * 0.05;
            System.out.println("ID discount: 5% off");
        }
        double total = subtotal - discount;

        System.out.printf("%nSubtotal: PHP %.2f%nDiscount: -PHP %.2f%nTOTAL:    PHP %.2f%n",
                subtotal, discount, total);

        // Payment
        System.out.println("\nPayment: 1. Cash  2. GCash (simulated)  3. Card (simulated)");
        int method = readInt("Choose payment: ", 1, 3);

        if (method == 1) {
            double paid = 0;
            // while loop: retry until enough cash is inserted
            while (paid < total) {
                paid = readDouble(String.format("Insert cash (need PHP %.2f): ", total));
                if (paid < total) {
                    System.out.println("Insufficient amount. Please try again.");
                }
            }
            System.out.printf("Change: PHP %.2f%n", paid - total);
        } else if (method == 2) {
            System.out.println("Scan the GCash QR code... Payment received!");
        } else {
            System.out.println("Tap or insert your card... Payment approved!");
        }

        System.out.println("\nOrder confirmed. Please keep your order summary for reference.");
        brewAll();
    }

    // ---------- BREWING SIMULATION ----------

    static void brewAll() {
        String[] steps = {
            "Grinding beans...",
            "Heating water...",
            "Extracting / brewing...",
            "Steaming milk...",
            "Adding finishing touches..."
        };

        for (int d = 0; d < orderNames.size(); d++) {
            System.out.println("\nBrewing drink " + (d + 1) + ": " + orderNames.get(d));

            // for loop: brewing progress bar
            for (int i = 0; i < steps.length; i++) {
                int percent = (i + 1) * 100 / steps.length;
                int filled = percent / 10;
                StringBuilder bar = new StringBuilder("[");
                for (int j = 0; j < 10; j++) {
                    bar.append(j < filled ? '#' : '-');
                }
                bar.append("] ").append(percent).append("%");
                System.out.println("Step " + (i + 1) + ": " + steps[i] + "  " + bar);
                pause(600);
            }
            System.out.println("Drink " + (d + 1) + " is ready! Enjoy!");
        }
        System.out.println("\nThank you for ordering at Smart Coffee Kiosk!");
        pause(1000);
    }

    // ---------- HELPERS ----------

    static void printBanner() {
        System.out.println("\n****************************************");
        System.out.println("*      SMART AUTOMATED COFFEE KIOSK    *");
        System.out.println("****************************************");
    }

    static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Please enter a number from " + min + " to " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Numbers only, please.");
            }
        }
    }

    static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(sc.nextLine().trim());
                if (Double.isFinite(value) && value >= 0) {
                    return value;
                }
                System.out.println("Amount must be a finite, non-negative number.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Enter a number such as 150 or 150.50.");
            }
        }
    }

    static boolean askYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String ans = sc.nextLine().trim().toLowerCase();
            if (ans.equals("y") || ans.equals("yes")) {
                return true;
            } else if (ans.equals("n") || ans.equals("no")) {
                return false;
            }
            System.out.println("Please answer y or n.");
        }
    }

    static void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
