# Session 1 Extension — UI to REST API Integration Walkthrough

## Teaching purpose

Students have already learned the REST endpoint syntax. This extension makes the client side visible so they can connect the concepts:

```text
User interaction
      ↓
HTML + JavaScript
      ↓
fetch(...)
      ↓
HTTP request
      ↓
@RestController
      ↓
JSON response
      ↓
JavaScript object
      ↓
DOM update
```

The **user-facing product UI is intentionally at the top of the home page**. Students first see what an end user does, then inspect the network/API activity underneath.

## Demo 1 — Load products

1. Open `http://localhost:8080`.
2. The Product Browser automatically loads products.
3. Point to the Integration Trace.
4. Explain the frontend code in `static/ui-integration.js`:

```javascript
const response = await fetch('/api/products');
const products = await response.json();
renderProducts(products);
```

5. Open `ProductRestController#getAll`:

```java
@GetMapping
public List<ProductResponse> getAll(...) {
    return productService.findAllActive(...);
}
```

Key message: **the API returns data; JavaScript creates the UI.**

## Demo 2 — Filter by brand (`@RequestParam`)

1. Enter `Wusthof` in the brand field.
2. Click **Apply Filter**.
3. The browser calls:

```text
GET /api/products?brand=Wusthof
```

4. Connect the query string to:

```java
@RequestParam(required = false) String brand
```

5. Show that only the relevant cards are rendered.

## Demo 3 — View one product (`@PathVariable`)

1. Click **View API** on a product card.
2. The Integration Trace shows:

```text
GET /api/products/{id}
```

3. Connect `{id}` to:

```java
@PathVariable Long id
```

## Demo 4 — Create a product (`@RequestBody` + JSON)

1. Fill the **Create Product** form.
2. Click **Create Product**.
3. Explain how browser form values become a JavaScript object.
4. `JSON.stringify(payload)` creates the JSON request body.
5. `fetch()` sends `POST /api/products` with `Content-Type: application/json`.
6. Spring/Jackson converts JSON to `ProductRequest` through `@RequestBody`.
7. Observe `201 Created`, the `Location` header, and the returned JSON.
8. The UI automatically reloads the product list.

## Demo 5 — Validation failure

1. Set Name to blank or Price to `-1`.
2. Submit the form.
3. Observe the `400` JSON error in the Integration Trace.
4. Follow the server flow:

```text
@Valid
  ↓
validation exception
  ↓
@RestControllerAdvice
  ↓
consistent ErrorResponse JSON
```

The UI does not need to know Java exception classes. It only consumes the HTTP status and error contract.

## Demo 6 — Partial update with PATCH

1. Click **+1 Stock** on a product card.
2. The browser sends only the changed field:

```json
{
  "stockQuantity": 13
}
```

3. Connect this to `PATCH /api/products/{id}` and `ProductPatchRequest`.
4. Observe that the other product fields are not sent again.
5. The product card is refreshed from the API without a page reload.

## Demo 7 — Delete

1. Click **Delete** on a product card.
2. The browser sends:

```text
DELETE /api/products/{id}
```

3. Observe `204 No Content`.
4. JavaScript calls the collection endpoint again and updates the product cards.

## Source files to walk through

```text
src/main/resources/static/index.html
    User-facing HTML elements

src/main/resources/static/ui-integration.js
    Browser event handlers, fetch(), JSON conversion, DOM rendering

src/main/java/.../controller/ProductRestController.java
    HTTP endpoint mapping

src/main/java/.../service/ProductService.java
    Business/application logic
```

## Core learning message

A REST UI integration has **two separate responsibilities**:

```text
Spring Boot REST API
- accepts HTTP requests
- runs backend logic
- returns status / headers / data

Frontend JavaScript
- listens to user actions
- calls the API
- converts JSON to JavaScript objects
- decides how to display the result
```

This separation is why the same REST API can later be consumed by a browser SPA, mobile app, or partner system.
