GOODBEAN COFFEE - REVISED PROJECT

Files
- index.html: responsive coffee ordering website (HTML, CSS, JavaScript in one file)
- CoffeeKiosk.java: Java console version of the Smart Automated Coffee Ordering Kiosk

Website improvements included
- Added a live menu search field and result count.
- Added an empty-search-results state.
- Replaced drink icons with optimized local drink photographs from Unsplash.
- Fixed the Frappuccino product-art background class.
- Improved cart alternative-milk stock handling when increasing, decreasing, or removing cart items.
- Moved the order summary and payment checkout to a separate page from the menu.
- Made the welcome hero a full-screen homepage; the menu opens from its featured button.
- Added itemized, printable receipts with order references, payment details, discounts, and change.
- Added a browser-saved rewards balance: earn 1 point per ₱10 spent and redeem 100 points for ₱50 off orders of at least ₱50.
- Applied the forest-green (#305B43), warm-cream (#F6F2EB), caramel (#F4C38D), and white (#FFFFFF) brand palette throughout the website.
- Preserved the existing customization/checkout flow.

Java improvement included
- Validated cash input to reject negative, NaN, and infinite values.

Run the website
1. Extract this folder.
2. Open index.html in a modern browser.

Run the Java console program
1. Install a JDK (Java 11 or newer recommended).
2. Open a terminal in this folder.
3. Run: javac CoffeeKiosk.java
4. Run: java CoffeeKiosk

Notes
- GCash and card payment are simulations only; this project does not process real payments.
- Receipts can be printed or saved as PDF using the browser's print dialog. Rewards balances are stored in the current browser on this device; there are no customer accounts or cross-device syncing.
- Student/staff ID eligibility is represented by a checkbox in the demo and is not externally verified.
- The website and Java console application are separate front ends/demos; the HTML page does not call the Java class or a backend server.
