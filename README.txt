GOODBEAN COFFEE - REVISED PROJECT

Files
- index.html: responsive coffee ordering website (HTML, CSS, JavaScript in one file)
- CoffeeKiosk.java: Java console version of the Smart Automated Coffee Ordering Kiosk

Website improvements included
- Added a live menu search field and result count.
- Added an empty-search-results state.
- Improved the menu with clearer headings, larger drink photos, category badges, responsive filters, and prominent customization buttons.
- Replaced drink icons with optimized local drink photographs from Unsplash.
- Fixed the Frappuccino product-art background class.
- Improved cart alternative-milk stock handling when increasing, decreasing, or removing cart items.
- Moved the order summary and payment checkout to a separate page from the menu.
- Made the welcome hero a full-screen homepage with larger headline, supporting copy, coffee artwork, and service highlights; the menu opens from its featured button.
- Added itemized, printable receipts with order references, payment details, discounts, and change.
- Added a browser-saved rewards balance: earn 1 point per ₱10 spent and redeem 100 points for ₱50 off orders of at least ₱50.
- Applied the forest-green (#305B43), warm-cream (#F6F2EB), caramel (#F4C38D), and white (#FFFFFF) brand palette throughout the website.
- Preserved the existing customization/checkout flow.
- Connected checkout to a Java Spring Boot API. The API validates products and customizations, calculates prices and discounts, checks cash payments, and returns receipt and rewards details.

Java improvement included
- Validated cash input to reject negative, NaN, and infinite values.
- Added a Spring Boot HTTP API with health and menu endpoints.
- Added a Dockerfile for deploying the Java backend and website together on Render.

Run the website
1. Extract this folder.
2. Open index.html in a modern browser.

Run the Java console program
1. Install a JDK (Java 11 or newer recommended).
2. Open a terminal in this folder.
3. Run: javac CoffeeKiosk.java
4. Run: java CoffeeKiosk

Run the Java website API locally
1. Install JDK 21 and Maven.
2. In this folder, run: mvn spring-boot:run
3. Set window.GOODBEAN_API_URL in api-config.js to http://localhost:10000.
4. Serve the website from http://localhost:5500 or http://localhost:5173.

Deploy the website and Java API together on Render
1. Push this project to GitHub and deploy the repository's Dockerfile as a Render web service.
2. The Docker image bundles index.html and drink photos with the Java API.
3. Open the Render service URL (for example, https://goodbean-java-api.onrender.com/) to see the website. The API health check remains at /api/health.
4. The same website can remain deployed on Vercel; api-config.js automatically uses the same-origin API on Render and the Render API URL on Vercel.

Notes
- GCash and card payment are simulations only; this project does not process real payments.
- Receipts can be printed or saved as PDF using the browser's print dialog. Rewards balances are stored in the current browser on this device; there are no customer accounts or cross-device syncing.
- Student/staff ID eligibility is represented by a checkbox in the demo and is not externally verified.
- The legacy CoffeeKiosk.java console program remains a separate demonstration. The deployed website calls the Spring Boot API instead.
- Orders are not stored in a database, rewards are still browser-local, and payment methods other than cash remain simulated. The Render API is a demo backend, not production payment processing.
