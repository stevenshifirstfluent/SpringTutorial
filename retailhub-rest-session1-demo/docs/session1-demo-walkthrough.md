# Session 1 Live Demo Walkthrough

## Demo 1 — Why REST?

**Business question:** RetailHub already has a Spring MVC application. Why add REST?

Open:

- `/mvc/products` — server returns HTML through `@Controller` and Thymeleaf.
- `/api/products` — server returns JSON through `@RestController`.

Teaching statement:

> We are not replacing the service or repository layers. We are changing how external clients communicate with the backend.

## Demo 2 — Resource + HTTP Method

Use the top interaction panel:

```text
GET /api/products
GET /api/products/1
POST /api/products
PUT /api/products/1
PATCH /api/products/1
DELETE /api/products/1
```

Teaching statement:

> The URI identifies the resource. The HTTP method expresses the intent.

## Demo 3 — Request data sources

### Path variable

```text
GET /api/products/1
```

Maps to:

```java
@GetMapping("/{id}")
public ResponseEntity<ProductResponse> getById(@PathVariable Long id)
```

### Query parameter

```text
GET /api/products?brand=Wusthof
```

Maps to:

```java
@RequestParam(required = false) String brand
```

### Request body

```text
POST /api/products
Content-Type: application/json
```

Maps to:

```java
@Valid @RequestBody ProductRequest request
```

## Demo 4 — HTTP response

Create a product and point out:

```text
201 Created
Location: /api/products/{newId}
Content-Type: application/json
```

Teaching statement:

> A REST response is not only JSON. It is status + headers + an optional body.

## Demo 5 — Validation and errors

Use the **Validation error** scenario.

Expected flow:

```text
Invalid JSON fields
      -> @Valid
      -> MethodArgumentNotValidException
      -> @RestControllerAdvice
      -> 400 Bad Request + consistent ErrorResponse JSON
```

Use **404 Not found** next:

```text
GET /api/products/999
      -> ProductNotFoundException
      -> @RestControllerAdvice
      -> 404 Not Found
```

## Demo 6 — PUT vs PATCH

PUT example replaces all editable fields supplied by `ProductRequest`.

PATCH example changes only fields present in `ProductPatchRequest`.

## Demo 7 — DELETE

```text
DELETE /api/products/1
```

Returns:

```text
204 No Content
```

The row is retained in H2 with `active=false` so the class can discuss soft delete without mixing this concern into the REST semantics.
