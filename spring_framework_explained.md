# Spring Framework Runtime — Terms Explained with Concrete Examples

This note explains the major terms shown in the Spring Framework Runtime diagram.  
The goal is not only to define each module, but to show **why it exists**, **how it is used**, and **what a concrete Spring example looks like**.




---

# Table of Contents

- [1. Core Container](#1-core-container)
  - [1.1 Core](#11-core)
- [2. Spring Bean](#2-spring-bean)
  - [What is a Bean?](#what-is-a-bean)
  - [2.1 Creating a Bean with Component Scanning](#21-creating-a-bean-with-component-scanning)
  - [2.2 Creating a Bean Explicitly with `@Bean`](#22-creating-a-bean-explicitly-with-bean)
- [3. ApplicationContext](#3-applicationcontext)
  - [What is `ApplicationContext`?](#what-is-applicationcontext)
  - [3.1 Why Do We Need an ApplicationContext?](#31-why-do-we-need-an-applicationcontext)
  - [3.2 How `ApplicationContext` Works with Beans](#32-how-applicationcontext-works-with-beans)
  - [3.3 Creating an ApplicationContext Manually](#33-creating-an-applicationcontext-manually)
  - [3.4 ApplicationContext in Spring Boot](#34-applicationcontext-in-spring-boot)
  - [3.5 ApplicationContext vs Bean](#35-applicationcontext-vs-bean)
  - [3.6 ApplicationContext and Dependency Injection](#36-applicationcontext-and-dependency-injection)
  - [3.7 Key Takeaway](#37-key-takeaway)
- [4. IoC — Inversion of Control](#4-ioc-inversion-of-control)
  - [What is IoC?](#what-is-ioc)
  - [4.1 The Problem Without IoC](#41-the-problem-without-ioc)
  - [4.2 Move Object Creation Outside the Class](#42-move-object-creation-outside-the-class)
  - [4.3 IoC in Spring](#43-ioc-in-spring)
  - [4.4 Why IoC is Useful](#44-why-ioc-is-useful)
  - [4.5 Benefit 1 — Easier to Change Implementations](#45-benefit-1-easier-to-change-implementations)
  - [4.6 Benefit 2 — Easier to Test](#46-benefit-2-easier-to-test)
  - [4.7 Benefit 3 — Centralised Object Management](#47-benefit-3-centralised-object-management)
  - [4.8 Benefit 4 — Business Classes Stay Focused](#48-benefit-4-business-classes-stay-focused)
  - [4.9 IoC and Dependency Injection](#49-ioc-and-dependency-injection)
  - [4.10 Before and After IoC](#410-before-and-after-ioc)
  - [4.11 Key Takeaway](#411-key-takeaway)
- [5. SpEL — Spring Expression Language](#5-spel-spring-expression-language)
- [6. Data Access / Integration](#6-data-access-integration)
- [7. Spring JDBC](#7-spring-jdbc)
  - [What is JDBC?](#what-is-jdbc)
  - [7.1 JDBC Driver Dependency](#71-jdbc-driver-dependency)
  - [7.2 Database Connection Configuration](#72-database-connection-configuration)
  - [7.3 JDBC in a Modern Spring Application](#73-jdbc-in-a-modern-spring-application)
  - [7.4 Direct JDBC](#74-direct-jdbc)
  - [7.5 Key Takeaway](#75-key-takeaway)
- [8. ORM — Object Relational Mapping](#8-orm-object-relational-mapping)
  - [JPA and Hibernate](#jpa-and-hibernate)
- [9. Spring Data JPA](#9-spring-data-jpa)
- [10. Transactions](#10-transactions)
  - [Without Proper Transaction Handling](#without-proper-transaction-handling)
  - [With Spring](#with-spring)
- [11. OXM — Object/XML Mapping](#11-oxm-objectxml-mapping)
- [12. JMS — Java Message Service](#12-jms-java-message-service)
- [13. Web Layer](#13-web-layer)
  - [13.1 Why Do We Need the Web Module?](#131-why-do-we-need-the-web-module)
  - [13.2 Spring Web and Servlet](#132-spring-web-and-servlet)
  - [13.3 Spring Web and Spring MVC](#133-spring-web-and-spring-mvc)
  - [13.4 What Does Spring Web Provide?](#134-what-does-spring-web-provide)
  - [13.5 Spring Web vs Spring MVC](#135-spring-web-vs-spring-mvc)
  - [13.6 Key Takeaway](#136-key-takeaway)
- [14. Servlet](#14-servlet)
- [15. Spring MVC](#15-spring-mvc)
  - [Typical Spring MVC Flow](#typical-spring-mvc-flow)
- [16. DispatcherServlet](#16-dispatcherservlet)
- [17. WebSocket](#17-websocket)
- [18. Portlet](#18-portlet)
- [19. AOP — Aspect-Oriented Programming](#19-aop-aspect-oriented-programming)
  - [Problem Without AOP](#problem-without-aop)
  - [With AOP](#with-aop)
- [20. Spring AOP Proxy](#20-spring-aop-proxy)
- [21. Aspect](#21-aspect)
- [22. Advice](#22-advice)
- [23. Pointcut](#23-pointcut)
- [24. Join Point](#24-join-point)
  - [AOP Terminology Together](#aop-terminology-together)
- [25. `@Around` Example](#25-around-example)
- [26. Aspects Module](#26-aspects-module)
- [27. Instrumentation](#27-instrumentation)
- [28. Messaging](#28-messaging)
  - [Messaging vs JMS](#messaging-vs-jms)
- [29. Spring Security](#29-spring-security)
  - [29.1 Authentication](#291-authentication)
  - [29.2 Authorization](#292-authorization)
  - [Typical Security Flow](#typical-security-flow)
- [30. Spring Test](#30-spring-test)
  - [Focused Testing](#focused-testing)
- [31. Spring Framework vs Spring Boot](#31-spring-framework-vs-spring-boot)
  - [Spring Framework](#spring-framework)
  - [Spring Boot](#spring-boot)
- [32. End-to-End Concrete Example](#32-end-to-end-concrete-example)
- [33. How Everything Fits Together](#33-how-everything-fits-together)
- [34. Recommended Learning Priority](#34-recommended-learning-priority)
- [35. Quick Reference](#35-quick-reference)
- [36. One-Sentence Mental Model](#36-one-sentence-mental-model)

---

# 1. Core Container

The **Core Container** is the foundation of the Spring Framework.

It is responsible for:

- creating application objects,
- managing Spring Beans,
- resolving dependencies,
- configuring application objects,
- managing object lifecycle.

The important terms in this area are:

- Core
- Beans
- Context
- SpEL

---

## 1.1 Core

### What it means

The Spring Core module provides the fundamental functionality used by the rest of Spring.

The most important idea is **Dependency Injection (DI)**.

Dependency Injection itself is not unique to Spring.

### Without Spring

```java
public class PaymentService {
    public void pay() {
        System.out.println("Payment completed");
    }
}
```

```java
public class OrderService {

    private final PaymentService paymentService;

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void placeOrder() {
        paymentService.pay();
        System.out.println("Order placed");
    }
}
```

The constructor already supports dependency injection.

But you must create the objects yourself:

```java
PaymentService paymentService = new PaymentService();

OrderService orderService =
        new OrderService(paymentService);

orderService.placeOrder();
```

### With Spring

```java
@Service
public class PaymentService {

    public void pay() {
        System.out.println("Payment completed");
    }
}
```

```java
@Service
public class OrderService {

    private final PaymentService paymentService;

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

Spring creates the objects and connects them.

Conceptually, Spring performs something similar to:

```java
PaymentService paymentService = new PaymentService();
OrderService orderService = new OrderService(paymentService);
```

The important difference is:

> Java supports constructor-based dependency injection. Spring automates object creation and dependency wiring.

---

# 2. Spring Bean

## What is a Bean?

A **Spring Bean** is simply an object whose creation and lifecycle are managed by the Spring container.

For example:

```java
@Service
public class PaymentService {
}
```

Spring discovers `PaymentService` and creates an instance of it.

Conceptually:

```text
Spring Container
      |
      +-- PaymentService bean
      +-- OrderService bean
      +-- CustomerService bean
```

---

## 2.1 Creating a Bean with Component Scanning

Spring commonly discovers beans through annotations.

### `@Component`

```java
@Component
public class EmailClient {
}
```

### `@Service`

```java
@Service
public class PaymentService {
}
```

### `@Repository`

```java
@Repository
public class CustomerRepository {
}
```

### `@Controller`

```java
@Controller
public class CustomerController {
}
```

All of these create Spring-managed components.

---

## 2.2 Creating a Bean Explicitly with `@Bean`

Spring commonly creates Beans by scanning classes marked with annotations such as:

```java
@Component
@Service
@Repository
@Controller
```

For example:

```java
@Service
public class PaymentService {
}
```

When Spring scans the application, it detects `PaymentService`, creates an instance, and registers that object in the Spring container as a Bean.

However, not every object can or should be created through component scanning.

A common case is an object from an external library. Since the class is not part of your own source code, you normally would not modify it simply to add a Spring annotation.

For example, suppose the application uses Jackson's `ObjectMapper`:

```java
ObjectMapper mapper = new ObjectMapper();
```

If you want Spring to create and manage this object, you can define it explicitly in a configuration class:

```java
@Configuration
public class AppConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
```

The `@Bean` annotation tells Spring:

> **Register the object returned by this method in the Spring container and manage it as a Bean.**

Conceptually, the method:

```java
@Bean
public ObjectMapper objectMapper() {
    return new ObjectMapper();
}
```

defines **how the object should be created**, while Spring takes responsibility for managing the returned object.

The resulting Bean can then be injected into another Spring-managed class:

```java
@Service
public class JsonService {

    private final ObjectMapper objectMapper;

    public JsonService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
```

Spring finds the `ObjectMapper` Bean created by the `@Bean` method and supplies it to the `JsonService` constructor.

The two common approaches can therefore be compared as follows:

```text
Component Scanning

@Service
@Component
@Repository
@Controller
        |
        v
Spring discovers the class
        |
        v
Spring creates and manages the Bean
```

```text
Explicit Bean Configuration

@Configuration
        |
      @Bean
        |
        v
The method defines how to create the object
        |
        v
Spring manages the returned object as a Bean
```

`@Bean` is also useful when object creation requires custom configuration.

For example:

```java
@Configuration
public class AppConfig {

    @Bean
    public ObjectMapper objectMapper() {

        ObjectMapper mapper = new ObjectMapper();

        mapper.findAndRegisterModules();

        return mapper;
    }
}
```

This allows the application to control how the object is constructed and configured before Spring manages it.

A useful distinction is:

> **Component scanning tells Spring which application classes should become Beans.**

> **`@Bean` explicitly tells Spring how to create a particular Bean.**

`@Bean` is therefore commonly used when:

- the class comes from a third-party library,
- the class cannot be annotated directly,
- object creation requires custom configuration,
- the application needs explicit control over how the Bean is constructed.
# 3. ApplicationContext

## What is `ApplicationContext`?

`ApplicationContext` is the main Spring container used in most Spring applications.

Its role is to provide a central place where Spring:

- creates Beans,
- keeps track of Beans,
- resolves dependencies between Beans,
- injects one Bean into another,
- manages Bean lifecycle,
- provides access to application configuration and resources.

A useful mental model is:

```text
ApplicationContext
      |
      +-- CustomerController
      |
      +-- CustomerService
      |
      +-- CustomerRepository
      |
      +-- PaymentService
      |
      +-- DataSource
```

The important point is that these objects are not simply stored in the container.  
Spring also understands **how they depend on one another**.

For example:

```text
CustomerController
        |
        v
CustomerService
        |
        v
CustomerRepository
```

Spring knows that:

- `CustomerController` needs `CustomerService`,
- `CustomerService` needs `CustomerRepository`.

The `ApplicationContext` coordinates the creation of these objects in the correct order and supplies the required dependencies.

---

## 3.1 Why Do We Need an ApplicationContext?

Without Spring, an application must create and connect its objects manually.

For example:

```java
CustomerRepository repository =
        new CustomerRepository();

CustomerService service =
        new CustomerService(repository);

CustomerController controller =
        new CustomerController(service);
```

This is manageable when the application is small.

As the application grows, the dependency graph becomes more complex:

```text
OrderController
      |
      v
OrderService
      |
      +-------------------+
      |                   |
      v                   v
PaymentService      InventoryService
      |                   |
      v                   v
PaymentRepository   InventoryRepository
```

Without a container, the application must decide:

- which object should be created first,
- which dependency should be passed to which object,
- whether an object should be reused or recreated,
- how configuration should be supplied,
- how object lifecycle should be managed.

The `ApplicationContext` centralizes these responsibilities.

Instead of manually building the object graph, application classes simply declare what they need.

Example:

```java
@Service
public class OrderService {

    private final PaymentService paymentService;
    private final InventoryService inventoryService;

    public OrderService(
            PaymentService paymentService,
            InventoryService inventoryService) {

        this.paymentService = paymentService;
        this.inventoryService = inventoryService;
    }
}
```

`OrderService` does not create its own dependencies.

It simply declares:

> **I need a `PaymentService` and an `InventoryService`.**

The `ApplicationContext` finds the corresponding Beans and provides them.

---

## 3.2 How `ApplicationContext` Works with Beans

Consider these classes:

```java
@Repository
public class CustomerRepository {
}
```

```java
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(
            CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
}
```

```java
@RestController
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(
            CustomerService customerService) {
        this.customerService = customerService;
    }
}
```

When the application starts, Spring performs the following conceptually:

```text
1. Scan application classes
        |
        v
2. Find Spring components
        |
        +-- CustomerRepository
        +-- CustomerService
        +-- CustomerController
        |
        v
3. Create CustomerRepository
        |
        v
4. Create CustomerService
   and inject CustomerRepository
        |
        v
5. Create CustomerController
   and inject CustomerService
```

The resulting object graph is managed by the `ApplicationContext`:

```text
ApplicationContext
      |
      +-- CustomerRepository
      |
      +-- CustomerService
      |      |
      |      +--> CustomerRepository
      |
      +-- CustomerController
             |
             +--> CustomerService
```

This is one of the main reasons Spring applications do not need to repeatedly use `new` to construct application services and repositories.

---

## 3.3 Creating an ApplicationContext Manually

In a basic Spring application, an `ApplicationContext` can be created manually.

Example:

```java
@Configuration
@ComponentScan("com.example")
public class AppConfig {
}
```

```java
ApplicationContext context =
        new AnnotationConfigApplicationContext(
            AppConfig.class
        );
```

Spring then scans the configured package, creates the Beans, and stores them in the context.

A Bean can be retrieved explicitly:

```java
PaymentService paymentService =
        context.getBean(PaymentService.class);
```

This shows that the `ApplicationContext` acts as a registry and manager of Spring Beans.

However, directly calling `getBean()` throughout application code is generally not the preferred approach.

Constructor injection is normally cleaner:

```java
@Service
public class OrderService {

    private final PaymentService paymentService;

    public OrderService(
            PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

The application asks for its dependencies through the constructor, and Spring supplies them automatically.

---

## 3.4 ApplicationContext in Spring Boot

In Spring Boot, the `ApplicationContext` is normally created automatically.

```java
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(
            Application.class,
            args
        );
    }
}
```

The call:

```java
SpringApplication.run(Application.class, args);
```

starts the Spring Boot application and creates the application context.

Conceptually:

```text
SpringApplication.run(...)
        |
        v
Create ApplicationContext
        |
        v
Scan for Beans
        |
        v
Create Beans
        |
        v
Resolve Dependencies
        |
        v
Application Ready
```

In a typical Spring Boot application, this happens automatically, so developers rarely create the `ApplicationContext` manually.

---

## 3.5 ApplicationContext vs Bean

These two terms are closely related but have different meanings.

```text
Bean
= one object managed by Spring
```

```text
ApplicationContext
= the container that manages all those Beans
```

For example:

```text
ApplicationContext
      |
      +-- OrderService Bean
      +-- PaymentService Bean
      +-- CustomerRepository Bean
      +-- EmailService Bean
```

A simple analogy is:

```text
ApplicationContext
= managed workspace

Bean
= managed object inside that workspace
```

The context knows:

- what Beans exist,
- how they are created,
- what dependencies they require,
- how those Beans are connected.

---

## 3.6 ApplicationContext and Dependency Injection

Dependency Injection relies on the container knowing both:

1. **what objects exist**, and
2. **what each object requires**.

For example:

```java
@Service
public class OrderService {

    private final PaymentService paymentService;

    public OrderService(
            PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

Spring sees that the constructor requires:

```text
PaymentService
```

The `ApplicationContext` searches for a compatible `PaymentService` Bean and injects it.

Conceptually:

```text
OrderService needs PaymentService
            |
            v
ApplicationContext
            |
            | finds
            v
PaymentService Bean
            |
            | injects
            v
OrderService
```

This is the connection between:

```text
Bean
      +
ApplicationContext
      +
Dependency Injection
      +
Inversion of Control
```

---

## 3.7 Key Takeaway

The `ApplicationContext` is the central Spring container that manages the application's Spring Beans and their relationships.

Its main value is that application classes no longer need to manage object creation and dependency wiring themselves.

A concise way to remember it is:

> **Beans are the objects Spring manages; `ApplicationContext` is the container that creates, connects, and manages them.**
# 4. IoC — Inversion of Control

## What is IoC?

IoC stands for:

> **Inversion of Control**

The idea is simple:

> **A class should not be responsible for creating and managing all of the objects it depends on. That responsibility can be moved outside the class.**

In Spring, this responsibility is handled by the Spring container.

---

## 4.1 The Problem Without IoC

Consider an `OrderService` that needs a payment service.

Without IoC, the class may create its dependency directly:

```java
public class OrderService {

    private final StripePaymentService paymentService =
            new StripePaymentService();

    public void placeOrder() {
        paymentService.pay();
    }
}
```

This works, but `OrderService` is now responsible for two things:

```text
OrderService
    |
    +-- business logic
    |
    +-- creating its dependency
```

It is also tightly coupled to a specific implementation:

```text
OrderService
      |
      v
StripePaymentService
```

If the application later changes from Stripe to PayPal, `OrderService` must also change.

---

## 4.2 Move Object Creation Outside the Class

A better design is to let `OrderService` declare what it needs:

```java
public class OrderService {

    private final PaymentService paymentService;

    public OrderService(
            PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

Now `OrderService` no longer creates the dependency.

Some external code can decide which implementation to provide:

```java
PaymentService paymentService =
        new StripePaymentService();

OrderService orderService =
        new OrderService(paymentService);
```

The control over object creation has moved from:

```text
OrderService
```

to:

```text
External code
```

This is the basic idea behind **Inversion of Control**.

---

## 4.3 IoC in Spring

Spring takes this idea further by letting the container create and connect the objects.

For example:

```java
@Service
public class StripePaymentService
        implements PaymentService {

    @Override
    public void pay() {
        System.out.println("Pay with Stripe");
    }
}
```

```java
@Service
public class OrderService {

    private final PaymentService paymentService;

    public OrderService(
            PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

Spring manages the creation and wiring:

```text
ApplicationContext
        |
        +-- creates StripePaymentService
        |
        +-- creates OrderService
        |
        +-- injects PaymentService
            into OrderService
```

The application class only declares its dependency.

Spring decides how that dependency is created and supplied.

---

## 4.4 Why IoC is Useful

The main benefit is **loose coupling**.

Without IoC:

```java
public class OrderService {

    private final StripePaymentService paymentService =
            new StripePaymentService();
}
```

`OrderService` depends directly on one concrete implementation.

With IoC:

```java
public class OrderService {

    private final PaymentService paymentService;

    public OrderService(
            PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

Now `OrderService` depends on an abstraction.

For example:

```java
public interface PaymentService {
    void pay();
}
```

Different implementations can exist:

```java
@Service
public class StripePaymentService
        implements PaymentService {

    public void pay() {
        System.out.println("Pay with Stripe");
    }
}
```

```java
@Service
public class PaypalPaymentService
        implements PaymentService {

    public void pay() {
        System.out.println("Pay with PayPal");
    }
}
```

Conceptually:

```text
OrderService
      |
      v
PaymentService
      |
      +--> StripePaymentService
      |
      +--> PaypalPaymentService
```

`OrderService` does not need to know how the selected implementation is created.

---

## 4.5 Benefit 1 — Easier to Change Implementations

Suppose the application initially uses Stripe:

```text
OrderService
      |
      v
StripePaymentService
```

Later, the application may need PayPal:

```text
OrderService
      |
      v
PaypalPaymentService
```

Because `OrderService` depends on `PaymentService` rather than a specific implementation, the service code does not need to be rewritten just because the implementation changes.

This makes the design more flexible.

---

## 4.6 Benefit 2 — Easier to Test

IoC also makes testing much easier.

Suppose the real payment service calls an external payment provider.

A unit test should usually avoid calling the real provider.

With constructor injection, a test can supply a fake implementation:

```java
public class MockPaymentService
        implements PaymentService {

    @Override
    public void pay() {
        System.out.println("Mock payment");
    }
}
```

Then:

```java
PaymentService paymentService =
        new MockPaymentService();

OrderService orderService =
        new OrderService(paymentService);
```

The test controls which dependency is used.

Without IoC, if `OrderService` creates the real payment service internally, replacing it for testing becomes much harder.

---

## 4.7 Benefit 3 — Centralised Object Management

Without IoC, object creation can become scattered across the application:

```text
OrderController
      |
      +-- new OrderService()
              |
              +-- new PaymentService()
                      |
                      +-- new PaymentRepository()
```

As the application grows, this object graph becomes increasingly difficult to manage.

With Spring:

```text
ApplicationContext
      |
      +-- PaymentRepository
      +-- PaymentService
      +-- OrderService
      +-- OrderController
```

The Spring container becomes the central place responsible for creating and connecting Spring-managed objects.

---

## 4.8 Benefit 4 — Business Classes Stay Focused

Without IoC, business classes may contain infrastructure or construction logic:

```java
public class OrderService {

    private final PaymentRepository repository;

    public OrderService() {
        this.repository =
            new PaymentRepository();
    }
}
```

With IoC:

```java
@Service
public class OrderService {

    private final PaymentRepository repository;

    public OrderService(
            PaymentRepository repository) {
        this.repository = repository;
    }
}
```

The class now focuses on its business responsibility.

It does not need to know how `PaymentRepository` is created.

---

## 4.9 IoC and Dependency Injection

IoC is the broader design principle.

Dependency Injection is one common way to implement IoC.

A useful relationship is:

```text
IoC
= move control of object creation outside the class

Dependency Injection
= provide dependencies from outside the class

ApplicationContext
= Spring container that performs IoC

Bean
= object managed by the container
```

Constructor injection is the most common example:

```java
public OrderService(
        PaymentService paymentService) {
    this.paymentService = paymentService;
}
```

The class declares what it needs, and the container provides it.

---

## 4.10 Before and After IoC

Without IoC:

```text
OrderService
    |
    +-- creates PaymentService
    +-- chooses implementation
    +-- manages dependency
    +-- contains business logic
```

With IoC:

```text
OrderService
    |
    +-- declares what it needs
    +-- contains business logic

ApplicationContext
    |
    +-- creates dependencies
    +-- chooses and manages Beans
    +-- injects dependencies
```

This separation is one of the key design ideas behind Spring.

---

## 4.11 Key Takeaway

The practical purpose of IoC is to reduce coupling.

By moving object creation and dependency management outside business classes, an application becomes:

- easier to change,
- easier to test,
- easier to configure,
- easier to maintain,
- easier to scale as the number of components grows.

A concise way to remember it is:

> **With IoC, application classes declare what they need; the Spring container decides how those dependencies are created and supplied.**
# 5. SpEL — Spring Expression Language

SpEL allows Spring to evaluate expressions dynamically.

SpEL stands for:

> **Spring Expression Language**

Example:

```java
@Value("#{10 * 20}")
private int result;
```

`result` becomes:

```text
200
```

Another example:

```java
@Value("#{systemProperties['user.home']}")
private String homeDirectory;
```

SpEL is commonly seen in:

- configuration,
- conditional expressions,
- security expressions,
- annotation attributes,
- dynamic bean configuration.

---

# 6. Data Access / Integration

This part of Spring helps applications interact with:

- relational databases,
- ORM frameworks,
- transaction systems,
- XML,
- messaging platforms.

Important modules include:

- JDBC
- ORM
- Transactions
- OXM
- JMS

---

# 7. Spring JDBC

## What is JDBC?

JDBC stands for:

> **Java Database Connectivity**

It is Java's standard API for communicating with relational databases.

Spring JDBC does **not** remove the need to configure the database connection.  
The application still needs:

- the appropriate JDBC driver dependency,
- the database connection URL,
- the database username,
- the database password or other authentication settings.

The key idea is:

> **JDBC is the underlying Java standard used to connect to and communicate with relational databases. Spring can provide additional support on top of JDBC, but using Spring does not remove JDBC itself.**

---

## 7.1 JDBC Driver Dependency

For example, when using MySQL with Maven:

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

The JDBC driver knows how Java should communicate with that particular database.

Different databases require different JDBC drivers.

Examples include:

```text
MySQL       -> MySQL JDBC Driver
PostgreSQL  -> PostgreSQL JDBC Driver
Oracle      -> Oracle JDBC Driver
SQL Server  -> Microsoft SQL Server JDBC Driver
```

---

## 7.2 Database Connection Configuration

In a Spring Boot application, the connection information is commonly defined in `application.properties`.

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.datasource.username=myuser
spring.datasource.password=mypassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

The same configuration can also be written in `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mydb
    username: myuser
    password: mypassword
    driver-class-name: com.mysql.cj.jdbc.Driver
```

This configuration tells the application:

```text
Which database?
    |
    +-- jdbc:mysql://localhost:3306/mydb

How to authenticate?
    |
    +-- username
    +-- password

Which JDBC driver?
    |
    +-- MySQL JDBC Driver
```

---

## 7.3 JDBC in a Modern Spring Application

Even when your application does not directly write JDBC code, JDBC is often still part of the database access stack.

For example, when using Spring Data JPA:

```java
public interface CustomerRepository
        extends JpaRepository<Customer, Long> {
}
```

your application may use the following stack:

```text
Spring Data JPA
      |
      v
     JPA
      |
      v
 Hibernate
      |
      v
    JDBC
      |
      v
JDBC Driver
      |
      v
 Database
```

So although the developer may work mainly with JPA or Spring Data JPA, JDBC is still used underneath to communicate with the relational database.

---

## 7.4 Direct JDBC

It is also possible to work directly with JDBC.

Example:

```java
Connection connection =
        DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/mydb",
            "myuser",
            "mypassword"
        );

PreparedStatement statement =
        connection.prepareStatement(
            "SELECT * FROM customer WHERE id = ?"
        );

statement.setLong(1, 100L);

ResultSet resultSet =
        statement.executeQuery();

if (resultSet.next()) {
    System.out.println(
        resultSet.getString("name")
    );
}

resultSet.close();
statement.close();
connection.close();
```

With direct JDBC, the developer handles tasks such as:

```text
Create connection
      |
Prepare SQL statement
      |
Set parameters
      |
Execute SQL
      |
Read ResultSet
      |
Handle errors
      |
Close resources
```

Spring provides optional helper APIs that can reduce this repetitive work, but they are **not required**.

---


## 7.5 Key Takeaway

Remember these three ideas:

1. **JDBC is Java's standard mechanism for connecting to relational databases.**
2. **The application still needs a JDBC driver, connection URL, and database credentials.**

# 8. ORM — Object Relational Mapping

ORM stands for:

> **Object-Relational Mapping**

It maps Java objects to relational database tables.

Suppose the database contains:

```text
CUSTOMER
----------------
id
name
email
```

You can represent it in Java:

```java
@Entity
public class Customer {

    @Id
    private Long id;

    private String name;

    private String email;
}
```

Instead of manually converting rows into objects, an ORM framework performs much of the mapping.

---

## JPA and Hibernate

A useful distinction is:

```text
JPA
= specification

Hibernate
= implementation of JPA
```

Typical stack:

```text
Spring Data JPA
      |
      v
     JPA
      |
      v
 Hibernate
      |
      v
    JDBC
      |
      v
 Database
```

---

# 9. Spring Data JPA

Although not explicitly shown as a box in the diagram, Spring Data JPA is extremely common in modern Spring Boot applications.

Example:

```java
public interface CustomerRepository
        extends JpaRepository<Customer, Long> {
}
```

You immediately get methods such as:

```java
customerRepository.findById(1L);
customerRepository.findAll();
customerRepository.save(customer);
customerRepository.delete(customer);
```

without implementing those methods manually.

You can even define queries through method names:

```java
List<Customer> findByName(String name);
```

---

# 10. Transactions

A database transaction groups multiple operations into one logical unit.

Consider a bank transfer:

```text
1. Deduct $100 from Account A
2. Add $100 to Account B
```

Both operations should succeed together.

---

## Without Proper Transaction Handling

If the first operation succeeds and the second fails:

```text
Account A: -$100
Account B: unchanged
```

The data becomes inconsistent.

---

## With Spring

```java
@Service
public class TransferService {

    @Transactional
    public void transfer(
            Long fromAccount,
            Long toAccount,
            BigDecimal amount) {

        accountRepository.debit(fromAccount, amount);

        accountRepository.credit(toAccount, amount);
    }
}
```

Conceptually:

```text
BEGIN TRANSACTION
       |
       +-- debit account A
       |
       +-- credit account B
       |
       +-- success --> COMMIT
       |
       +-- failure --> ROLLBACK
```

Spring transaction management is one of the most important real-world examples of Spring AOP.

---

# 11. OXM — Object/XML Mapping

OXM stands for:

> **Object/XML Mapping**

It converts between Java objects and XML.

For example:

```xml
<Customer>
    <id>100</id>
    <name>Steven</name>
</Customer>
```

can be converted into:

```java
Customer customer;
```

and a Java object can also be converted back into XML.

This was particularly important for:

- SOAP services,
- enterprise XML integrations,
- legacy enterprise systems.

It is less prominent in modern REST applications where JSON is more common.

---

# 12. JMS — Java Message Service

JMS stands for:

> **Java Message Service**

It is a Java standard API for sending and receiving messages.

Instead of directly calling another service:

```text
OrderService
     |
     v
PaymentService
```

an application can communicate asynchronously:

```text
OrderService
     |
     v
Message Queue
     |
     v
Payment Processor
```

Example:

```java
@Service
public class OrderPublisher {

    private final JmsTemplate jmsTemplate;

    public OrderPublisher(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void publish(Order order) {
        jmsTemplate.convertAndSend("orders", order);
    }
}
```

JMS has traditionally been associated with messaging systems such as ActiveMQ.

Modern Spring systems may also use:

- Kafka,
- RabbitMQ,
- cloud messaging systems.

These do not necessarily use JMS internally.

---

# 13. Web Layer

The **Spring Web module** provides the basic web infrastructure used by Spring applications.

It is not the same as Spring MVC.

A useful distinction is:

```text
Spring Web
= core web and HTTP infrastructure

Spring MVC
= higher-level framework for controllers, web applications, and REST APIs
```

Spring Web provides the underlying support that higher-level web features depend on.

Conceptually:

```text
Spring Web
   |
   +-- HTTP request / response support
   +-- Servlet-based web infrastructure
   +-- web application context support
   +-- multipart file upload support
   +-- HTTP headers and message handling
   +-- common web utilities
   |
   v
Spring MVC
   |
   v
Controllers / REST APIs / Web Applications
```

---

## 13.1 Why Do We Need the Web Module?

A web application needs infrastructure to deal with HTTP communication.

For example, when a browser or another application sends:

```http
GET /customers/100
```

the server needs to handle:

```text
HTTP Request
     |
     v
Web Infrastructure
     |
     +-- request information
     +-- URL
     +-- HTTP method
     +-- headers
     +-- parameters
     +-- request body
     |
     v
Application Logic
     |
     v
HTTP Response
```

Spring Web provides the common web foundation that allows the rest of the Spring web stack to work with this request-response model.

---

## 13.2 Spring Web and Servlet

A Servlet is part of the standard Java web platform.

Spring Web builds on Servlet-based infrastructure in traditional Spring MVC applications.

The relationship can be viewed as:

```text
Java Servlet API
       |
       v
Spring Web
       |
       v
Spring MVC
       |
       v
@Controller / @RestController
```

The Servlet API provides the low-level Java web model.

Spring Web adds Spring-specific web infrastructure and abstractions.

Spring MVC then provides the controller-based programming model that application developers commonly use.

---

## 13.3 Spring Web and Spring MVC

Spring MVC is built on top of the Spring Web foundation.

For example:

```java
@RestController
@RequestMapping("/customers")
public class CustomerController {

    @GetMapping("/{id}")
    public String getCustomer(
            @PathVariable Long id) {

        return "Customer " + id;
    }
}
```

This controller is part of Spring MVC.

However, the request still depends on the underlying web infrastructure provided by Spring Web and the Servlet environment.

Conceptually:

```text
Client
   |
   | HTTP Request
   v
Servlet Container
   |
   v
Spring Web
   |
   v
Spring MVC
   |
   v
Controller
```

So:

> **Spring Web provides the foundation; Spring MVC provides the controller-based framework built on top of it.**

---

## 13.4 What Does Spring Web Provide?

At a high level, the Spring Web module provides support for:

- HTTP request and response handling,
- Servlet-based web applications,
- web-specific application context support,
- HTTP headers,
- request and response bodies,
- multipart file uploads,
- web-related utility classes,
- integration used by higher-level Spring web technologies.

For example, when uploading a file in a Spring MVC application:

```java
@PostMapping("/upload")
public String upload(
        @RequestParam("file")
        MultipartFile file) {

    return file.getOriginalFilename();
}
```

The controller is a Spring MVC feature, while multipart request support relies on the underlying Spring web infrastructure.

---

## 13.5 Spring Web vs Spring MVC

A concise comparison is:

| Term | Role |
|---|---|
| **Servlet** | Standard Java technology for handling web requests and responses |
| **Spring Web** | Core Spring infrastructure for web and HTTP applications |
| **Spring MVC** | Higher-level Spring framework for controllers, web applications, and REST APIs |

The relationship is:

```text
Servlet
   |
   v
Spring Web
   |
   v
Spring MVC
   |
   v
Application Controller
```

---

## 13.6 Key Takeaway

The Spring Web module provides the core web infrastructure used by Spring applications.

It is important to distinguish it from Spring MVC:

> **Spring Web provides the underlying HTTP and web support, while Spring MVC uses that support to build controllers, web applications, and REST APIs.**
# 14. Servlet

A Servlet is part of the standard Java web platform.

It receives HTTP requests and generates HTTP responses.

Conceptually:

```text
Browser
   |
HTTP Request
   |
   v
Servlet
   |
   v
Java Code
   |
HTTP Response
   |
   v
Browser
```

Traditional Servlet code:

```java
public class CustomerServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        response.getWriter().println("Hello");
    }
}
```

Spring MVC is built on top of the Servlet infrastructure.

---

# 15. Spring MVC

MVC stands for:

> **Model — View — Controller**

In modern Spring applications, Spring MVC is commonly used to build REST APIs.

Example:

```java
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(
            CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    public Customer getCustomer(
            @PathVariable Long id) {

        return customerService.findById(id);
    }
}
```

Calling:

```http
GET /customers/100
```

might return:

```json
{
  "id": 100,
  "name": "Steven"
}
```

---

## Typical Spring MVC Flow

```text
HTTP Request
     |
     v
DispatcherServlet
     |
     v
Controller
     |
     v
Service
     |
     v
Repository
     |
     v
Database
```

The response travels back through the layers.

---

# 16. DispatcherServlet

`DispatcherServlet` is a central component of Spring MVC.

It acts as the **front controller**.

Instead of every controller independently receiving raw HTTP traffic:

```text
HTTP Request
      |
      v
DispatcherServlet
      |
      +--> CustomerController
      |
      +--> OrderController
      |
      +--> PaymentController
```

It coordinates request routing and response handling.

Spring Boot configures it automatically.

---

# 17. WebSocket

HTTP normally follows a request-response model.

```text
Client ---- request ----> Server
Client <--- response ---- Server
```

WebSocket creates a persistent two-way connection:

```text
Client <===============> Server
```

This is useful for:

- chat applications,
- real-time notifications,
- live dashboards,
- trading systems,
- collaborative applications.

Example use case:

```text
Stock Price Server
        |
        +--> price update
        +--> price update
        +--> price update
        |
        v
Browser Dashboard
```

The server can push information without waiting for a new HTTP request.

---

# 18. Portlet

A Portlet is a component designed for enterprise portal applications.

A portal page might contain:

```text
+---------------------------+
| Employee Portal           |
+---------------------------+
| HR Portlet                |
+---------------------------+
| News Portlet              |
+---------------------------+
| Leave Application Portlet |
+---------------------------+
```

Portlet technology is mainly associated with older enterprise portal platforms.

For modern Spring application development, it is generally considered legacy technology and is not a major learning priority.

---

# 19. AOP — Aspect-Oriented Programming

AOP stands for:

> **Aspect-Oriented Programming**

It is used to separate **cross-cutting concerns** from business logic.

Common cross-cutting concerns include:

- logging,
- security,
- transaction handling,
- performance measurement,
- auditing.

---

## Problem Without AOP

Suppose we have:

```java
@Service
public class OrderService {

    public void placeOrder(String product) {

        System.out.println("Start");

        System.out.println(
            "Placing order for " + product
        );

        System.out.println("End");
    }
}
```

And:

```java
@Service
public class PaymentService {

    public void makePayment(double amount) {

        System.out.println("Start");

        System.out.println(
            "Making payment: " + amount
        );

        System.out.println("End");
    }
}
```

Logging is repeated in every service.

---

## With AOP

Business class:

```java
@Service
public class OrderService {

    public void placeOrder(String product) {
        System.out.println(
            "Placing order for " + product
        );
    }
}
```

Aspect:

```java
@Aspect
@Component
public class LoggingAspect {

    @Before(
        "execution(* com.example.service.*.*(..))"
    )
    public void logBefore() {
        System.out.println(
            "Method is about to execute"
        );
    }
}
```

Calling:

```java
orderService.placeOrder("MacBook");
```

could produce:

```text
Method is about to execute
Placing order for MacBook
```

The business class does not contain the logging infrastructure.

---

# 20. Spring AOP Proxy

Spring commonly implements AOP through proxies.

Conceptually:

```text
Application
     |
     v
Spring Proxy
     |
     +-- Logging
     |
     +-- Security
     |
     +-- Transaction
     |
     v
Real OrderService
```

Your application may think it is calling:

```java
orderService.placeOrder();
```

but the call first goes through the Spring proxy.

---

# 21. Aspect

An **Aspect** contains a cross-cutting concern.

Example:

```java
@Aspect
@Component
public class PerformanceAspect {
}
```

Examples of aspects:

```text
LoggingAspect
SecurityAspect
TransactionAspect
AuditAspect
PerformanceAspect
```

---

# 22. Advice

Advice describes **what should happen** when an aspect is triggered.

Common advice types:

```java
@Before
@After
@AfterReturning
@AfterThrowing
@Around
```

Example:

```java
@Before("execution(* com.example.service.*.*(..))")
public void beforeMethod() {
    System.out.println("Before method");
}
```

---

# 23. Pointcut

A Pointcut defines **where the aspect should apply**.

Example:

```java
execution(* com.example.service.*.*(..))
```

It means, roughly:

> Apply to matching methods inside the service package.

---

# 24. Join Point

A Join Point is the actual execution point being intercepted.

For example:

```java
orderService.placeOrder();
```

The execution of `placeOrder()` is a join point.

---

## AOP Terminology Together

```text
Aspect
    PerformanceAspect

Advice
    measureExecutionTime()

Pointcut
    execution(* com.example.service.*.*(..))

Join Point
    OrderService.placeOrder()
```

---

# 25. `@Around` Example

`@Around` advice can run both before and after a method.

```java
@Aspect
@Component
public class PerformanceAspect {

    @Around(
        "execution(* com.example.service.*.*(..))"
    )
    public Object measureTime(
            ProceedingJoinPoint joinPoint)
            throws Throwable {

        long start =
                System.currentTimeMillis();

        Object result =
                joinPoint.proceed();

        long end =
                System.currentTimeMillis();

        System.out.println(
            joinPoint.getSignature().getName()
            + " took "
            + (end - start)
            + " ms"
        );

        return result;
    }
}
```

This can be used to measure performance without placing timing code inside each service.

---

# 26. Aspects Module

The Spring **Aspects** module provides integration with AspectJ.

Spring AOP and AspectJ are related but not identical.

A simplified distinction:

```text
Spring AOP
    commonly proxy-based
    mainly intercepts Spring-managed method calls

AspectJ
    more powerful aspect-oriented programming model
    can support deeper bytecode weaving
```

For most normal Spring Boot applications, Spring AOP is sufficient.

---

# 27. Instrumentation

Instrumentation refers to observing or modifying application classes at runtime.

Possible uses include:

- performance monitoring,
- tracing,
- class transformation,
- runtime diagnostics.

Conceptually:

```text
OrderService.placeOrder()
        |
        v
Instrumentation Agent
        |
        +-- execution time
        +-- tracing information
        +-- diagnostic data
```

Application developers usually interact with this module less frequently than MVC, JPA, Security, or Transactions.

---

# 28. Messaging

Spring Messaging provides common abstractions for message-based communication.

A typical message contains:

```text
Message
   |
   +-- Headers
   |
   +-- Payload
```

Example:

```text
Headers
    eventType = ORDER_CREATED

Payload
    {
        "orderId": 100
    }
```

The messaging abstraction is used in areas such as:

- WebSocket messaging,
- STOMP,
- message processing infrastructure.

---

## Messaging vs JMS

They are related but not the same.

```text
Spring Messaging
    generic Spring messaging abstraction

JMS
    standard Java API for message-oriented middleware
```

---

# 29. Spring Security

Spring Security provides security infrastructure for Spring applications.

The two most important concepts are:

- Authentication
- Authorization

---

## 29.1 Authentication

Authentication answers:

> **Who are you?**

Examples:

- username/password,
- JWT token,
- OAuth login,
- OpenID Connect.

---

## 29.2 Authorization

Authorization answers:

> **What are you allowed to do?**

Example:

```java
@PreAuthorize("hasRole('ADMIN')")
public void deleteCustomer(Long id) {
}
```

Only users with the required role can invoke the method.

---

## Typical Security Flow

```text
HTTP Request
     |
     v
Spring Security Filter Chain
     |
     v
Authentication
     |
     v
Authorization
     |
     v
Controller
     |
     v
Service
```

Spring Security can support:

- login,
- access control,
- roles,
- permissions,
- JWT,
- OAuth2,
- OpenID Connect,
- CSRF protection,
- method-level security.

---

# 30. Spring Test

Spring Test provides infrastructure for testing Spring applications.

Example:

```java
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    void shouldPlaceOrder() {
        orderService.placeOrder("MacBook");
    }
}
```

`@SpringBootTest` loads a Spring application context for the test.

---

## Focused Testing

### Controller test

```java
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {
}
```

### JPA test

```java
@DataJpaTest
class CustomerRepositoryTest {
}
```

Focused tests are usually faster because they load only part of the application infrastructure.

---

# 31. Spring Framework vs Spring Boot

This distinction is important.

## Spring Framework

Provides infrastructure such as:

```text
Dependency Injection
Spring MVC
Transactions
AOP
JDBC
ORM integration
Security integration
Testing support
```

## Spring Boot

Builds on Spring Framework and makes configuration easier.

For example, instead of manually configuring:

```text
Tomcat
DispatcherServlet
Jackson
Spring MVC
DataSource
JPA
```

Spring Boot can auto-configure much of this based on:

- dependencies,
- configuration properties,
- classes available on the classpath.

---

# 32. End-to-End Concrete Example

Consider an online shop.

```java
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(
            OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order create(
            @RequestBody OrderRequest request) {

        return orderService.createOrder(request);
    }
}
```

Service:

```java
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final PaymentService paymentService;

    public OrderService(
            OrderRepository orderRepository,
            PaymentService paymentService) {

        this.orderRepository = orderRepository;
        this.paymentService = paymentService;
    }

    @Transactional
    public Order createOrder(
            OrderRequest request) {

        Order order =
            new Order(request.product());

        orderRepository.save(order);

        paymentService.charge(
            request.amount()
        );

        return order;
    }
}
```

Repository:

```java
public interface OrderRepository
        extends JpaRepository<Order, Long> {
}
```

Security:

```java
@PreAuthorize("isAuthenticated()")
public Order createOrder(...) {
    ...
}
```

AOP:

```java
@Around(
    "execution(* com.example.service.*.*(..))"
)
public Object measurePerformance(
        ProceedingJoinPoint joinPoint)
        throws Throwable {

    long start = System.currentTimeMillis();

    Object result = joinPoint.proceed();

    long end = System.currentTimeMillis();

    System.out.println(
        "Execution time: "
        + (end - start)
    );

    return result;
}
```

---

# 33. How Everything Fits Together

```text
Client
  |
  | HTTP POST /orders
  v
Spring Security
  |
  v
DispatcherServlet
  |
  v
OrderController
  |
  v
Spring AOP Proxy
  |
  +-- logging
  +-- transaction
  +-- performance monitoring
  |
  v
OrderService
  |
  +-------------------+
  |                   |
  v                   v
OrderRepository   PaymentService
  |
  v
Spring Data JPA
  |
  v
Hibernate
  |
  v
JDBC
  |
  v
Database
```

All these objects are managed by:

```text
Spring ApplicationContext
```

which creates and connects:

```text
OrderController
      |
      v
OrderService
      |
      +--> OrderRepository
      |
      +--> PaymentService
```

---

# 34. Recommended Learning Priority

The learning priority below is mapped to the existing sections in this tutorial and aligned with the course sequence.

| Course Topic | Related Section(s) in This Tutorial | Priority |
|---|---|---|
| JPA Intro Part 1 | **8. ORM — Object Relational Mapping** | High |
| JPA Intro Part 2 | **8. ORM — Object Relational Mapping** | High |
| JPQL | **8. ORM — Object Relational Mapping** | High |
| Spring Boot MVC | **13. Web Layer**, **14. Servlet**, **15. Spring MVC**, **16. DispatcherServlet**, **31. Spring Framework vs Spring Boot** | High |
| Spring Boot MVC + Spring Data + Dependency Injection | **2. Spring Bean**, **3. ApplicationContext**, **4. IoC — Inversion of Control**, **9. Spring Data JPA**, **15. Spring MVC** | High |
| Spring Boot MVC Full Stack + JUnit | **15. Spring MVC**, **30. Spring Test**, **32. End-to-End Concrete Example**, **33. How Everything Fits Together** | High |
| Spring Boot MVC + Service Layer | **4. IoC — Inversion of Control**, **15. Spring MVC**, **32. End-to-End Concrete Example**, **33. How Everything Fits Together** | High |
| Spring Boot Workshops | Review and apply **2–10**, **15–16**, **30–33** | High |
| Spring Session / Validation / Interceptors | **15. Spring MVC**, **16. DispatcherServlet** | Medium |
| Spring Transactions + Service Layer | **10. Transactions**, **19. AOP**, **20. Spring AOP Proxy**, **32. End-to-End Concrete Example** | High |
| Spring REST API | **13. Web Layer**, **14. Servlet**, **15. Spring MVC**, **16. DispatcherServlet**, **32. End-to-End Concrete Example** | High |
| Spring Reactive | Related to the web and messaging concepts in **13. Web Layer**, **17. WebSocket**, and **28. Messaging** | Later / Advanced |

Based on this course flow, the existing tutorial sections can be studied in the following order:

1. **8. ORM — Object Relational Mapping**
   - JPA fundamentals
   - Hibernate as a JPA implementation
   - persistence concepts

2. **9. Spring Data JPA**
   - repository abstraction
   - integration between Spring and JPA

3. **7. Spring JDBC**
   - understand JDBC as the underlying Java database connectivity mechanism
   - focus on the JDBC driver, connection configuration, and its relationship to JPA

4. **31. Spring Framework vs Spring Boot**
   - understand what Spring Boot adds on top of the Spring Framework

5. **2. Spring Bean**
   - understand Spring-managed objects

6. **3. ApplicationContext**
   - understand the role of the Spring container

7. **4. IoC — Inversion of Control**
   - understand why dependency creation is moved to the container
   - understand Dependency Injection and loose coupling

8. **13. Web Layer**

9. **14. Servlet**

10. **15. Spring MVC**

11. **16. DispatcherServlet**
    - understand the MVC request flow

12. **32. End-to-End Concrete Example**
    - connect Controller, Service, Repository, JPA, and Dependency Injection

13. **33. How Everything Fits Together**
    - consolidate the complete application structure

14. **30. Spring Test**
    - JUnit and Spring-aware testing

15. **10. Transactions**
    - transaction boundaries in the service layer

16. **19–25. AOP**
    - understand how Spring can apply cross-cutting behaviour such as transaction management

17. **29. Spring Security**
    - authentication and authorization

18. **17. WebSocket** and **28. Messaging**
    - real-time and message-based communication

19. **5. SpEL**, **11. OXM**, **12. JMS**, **26. Aspects Module**, **27. Instrumentation**
    - useful supporting or advanced topics

20. **18. Portlet**
    - legacy awareness only

The key emphasis for this course is:

> **JPA and persistence concepts come first, followed by Spring Boot MVC, Spring-managed components and Dependency Injection, then full-stack layering, testing, transactions, REST, and more advanced Spring capabilities.**

The tutorial should therefore be used as a **reference map of Spring concepts**, while the course sequence determines when each section is introduced in depth.
# 35. Quick Reference

| Term | Main Purpose |
|---|---|
| Core | Fundamental Spring infrastructure |
| Bean | Object managed by Spring |
| Context | Container holding and managing beans |
| IoC | Spring controls object creation |
| DI | Dependencies are supplied to objects |
| SpEL | Dynamic Spring expressions |
| JDBC | Relational database access |
| ORM | Maps Java objects to database tables |
| JPA | Java persistence specification |
| Hibernate | Popular JPA implementation |
| Transaction | Makes multiple operations atomic |
| OXM | Java object ↔ XML mapping |
| JMS | Java messaging standard |
| Servlet | Standard Java HTTP request handler |
| Spring MVC | Web and REST application framework |
| DispatcherServlet | Front controller for Spring MVC |
| WebSocket | Persistent two-way communication |
| Portlet | Legacy portal component technology |
| AOP | Separates cross-cutting concerns |
| Aspect | A cross-cutting behavior module |
| Advice | What should happen |
| Pointcut | Where an aspect should apply |
| Join Point | Actual intercepted execution point |
| Instrumentation | Runtime observation/transformation |
| Messaging | Generic Spring message abstraction |
| Spring Security | Authentication and authorization |
| Spring Test | Spring-aware testing support |
| Spring Boot | Easier configuration of Spring applications |

---

# 36. One-Sentence Mental Model

A useful overall mental model is:

> **Spring creates and connects your application objects, provides reusable infrastructure around them, and lets your business code focus primarily on business logic.**

And Spring Boot adds:

> **Convention, auto-configuration, and sensible defaults so that using the Spring Framework requires much less manual setup.**
