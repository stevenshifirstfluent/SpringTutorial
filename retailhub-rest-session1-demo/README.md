# RetailHub REST API — Session 1 Demo Project

A demo-ready Spring Boot project that follows the **RESTful API Session 1** teaching flow.

## Business use case

RetailHub originally served product pages with Spring MVC and Thymeleaf. The business now needs the same product capability to serve a modern web frontend, mobile applications, and partner systems. Those clients need **data and operations over HTTP**, not server-rendered HTML.

This project keeps the familiar backend layering:

```text
@RestController -> ProductService -> ProductRepository -> H2
```

and demonstrates how the **external interface** changes when we expose the application as a REST API.

## Learning goals covered

- Why REST is needed when clients need data instead of HTML
- `@Controller` vs `@RestController`
- Resource-oriented URLs
- GET / POST / PUT / PATCH / DELETE
- `@RequestMapping`, `@GetMapping`, `@PostMapping`, `@PutMapping`, `@PatchMapping`, `@DeleteMapping`
- `@PathVariable`, `@RequestParam`, `@RequestBody`
- JSON <-> Java conversion through Jackson
- HTTP status + headers + body
- `ResponseEntity`
- Bean Validation with `@Valid`
- Consistent errors with `@RestControllerAdvice`
- Soft delete as used in the Session 1 product example

## Technology

- Java 21
- Spring Boot 4.1.1
- Spring MVC
- Spring Data JPA
- Jakarta Bean Validation
- Thymeleaf (only for the MVC comparison page)
- H2 in-memory database

## Run

Open the project in IntelliJ IDEA / Eclipse / Spring Tool Suite and run:

```text
Session1RestDemoApplication.java
```

Or, with Maven installed:

```bash
mvn spring-boot:run
```

Then open:

```text
http://localhost:8080
```

The **user interaction panel is intentionally placed at the top** so the lecturer can perform actions first and explain the request/response flow underneath.


## UI -> REST API integration demo

The home page now starts with a **real user-facing Product Browser and Create Product form**. This is deliberately placed above the raw API playground so students can start from the user action and work downward into the HTTP/API implementation.

The frontend teaching file is:

```text
src/main/resources/static/ui-integration.js
```

It demonstrates the complete browser flow:

```text
User action
   -> JavaScript event handler
   -> fetch('/api/...')
   -> Spring @RestController
   -> JSON response
   -> response.json()
   -> DOM update
```

Demo interactions include:

- Load products with `GET /api/products`
- Filter with `GET /api/products?brand=...` and `@RequestParam`
- View one product with `GET /api/products/{id}` and `@PathVariable`
- Create through an HTML form with `POST`, `JSON.stringify(...)`, `@RequestBody` and Jackson
- Trigger Bean Validation and observe the JSON error contract
- Partially update stock with `PATCH /api/products/{id}` and a small JSON body
- Delete a product with `DELETE /api/products/{id}` and observe `204 No Content`

A lecturer walkthrough is provided in `docs/ui-api-integration-walkthrough.md`.

## Recommended walkthrough

1. **Compare MVC with REST**
   - Open `/mvc/products` — `@Controller` returns a view.
   - Call `/api/products` — `@RestController` returns JSON.

2. **GET all products**
   - `GET /api/products`
   - Observe default `200 OK` and JSON array.

3. **Path variable**
   - `GET /api/products/1`
   - Connect `{id}` to `@PathVariable Long id`.

4. **Query parameter**
   - `GET /api/products?brand=Wusthof`
   - Connect `?brand=` to `@RequestParam`.

5. **Create a resource**
   - `POST /api/products`
   - Observe `@RequestBody`, Jackson, `201 Created`, `Location` header and JSON body.

6. **Validation failure**
   - Use the “Validation error” scenario.
   - Observe `@Valid` -> `MethodArgumentNotValidException` -> `@RestControllerAdvice` -> `400` JSON error.

7. **PUT vs PATCH**
   - PUT replaces the editable representation.
   - PATCH changes only the supplied fields.

8. **DELETE**
   - `DELETE /api/products/1`
   - Observe `204 No Content` and soft delete (`active=false`).

9. **404 error**
   - `GET /api/products/999`
   - Observe domain exception -> consistent `404` error envelope.

10. **Reset**
   - Click **Reset Demo Data** to restore product IDs 1–5.

## REST endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/products` | Get active products |
| GET | `/api/products?brand=Wusthof` | Filter using `@RequestParam` |
| GET | `/api/products?categoryId=1` | Filter by category |
| GET | `/api/products/{id}` | Get one product using `@PathVariable` |
| POST | `/api/products` | Create a product |
| PUT | `/api/products/{id}` | Full replacement of editable fields |
| PATCH | `/api/products/{id}` | Partial update |
| DELETE | `/api/products/{id}` | Soft delete |
| POST | `/api/demo/reset` | Demo-only reset helper |

## H2 console

Open:

```text
http://localhost:8080/h2-console
```

Use:

```text
JDBC URL: jdbc:h2:mem:retailhub
User: sa
Password: (blank)
```

The console is provided only for teaching and database inspection.

## Project structure

```text
controller/
  ProductRestController.java     REST endpoints
  ProductMvcController.java      @Controller comparison
  DemoController.java            Demo reset helper
service/
  ProductService.java            business/application logic
  DemoDataService.java           predictable demo data
repository/
  ProductRepository.java         JPA data access
model/
  Product.java                   persistence entity
dto/
  ProductRequest.java            create/replace input
  ProductPatchRequest.java       partial update input
  ProductResponse.java           API response
  ErrorResponse.java             error contract
exception/
  ProductNotFoundException.java
  GlobalExceptionHandler.java
static/
  index.html / app.js / style.css
```

## Teaching design note

The original lecture examples sometimes call a repository directly from the controller to make annotation syntax easy to see. This demo preserves the same HTTP annotations and request/response examples, but keeps the previously learned layered architecture:

```text
Controller -> Service -> Repository
```

This prevents students from learning the incorrect rule that REST controllers should contain persistence or business logic.


## H2 Console

This project uses Spring Boot 4.x. In Spring Boot 4, the browser-based H2 console is provided by the separate `spring-boot-h2console` module, so the project includes both `com.h2database:h2` and `org.springframework.boot:spring-boot-h2console`.

Open: `http://localhost:8081/h2-console`

Use:

- JDBC URL: `jdbc:h2:mem:retailhub`
- User Name: `sa`
- Password: *(leave blank)*

The console is for Demo/development use only.

