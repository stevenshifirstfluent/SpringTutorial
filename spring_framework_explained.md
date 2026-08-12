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
  - [What it means](#what-it-means)
  - [Concrete Example](#concrete-example)
- [4. IoC — Inversion of Control](#4-ioc-inversion-of-control)
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

Sometimes you cannot annotate the class directly.

For example:

```java
@Configuration
public class AppConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
```

Spring now manages the returned `ObjectMapper`.

---

# 3. ApplicationContext

## What it means

`ApplicationContext` represents the Spring IoC container.

It keeps track of Spring Beans and their dependencies.

Example:

```text
ApplicationContext
      |
      +-- CustomerController
      |
      +-- CustomerService
      |
      +-- CustomerRepository
      |
      +-- DataSource
```

---

## Concrete Example

```java
@Configuration
@ComponentScan("com.example")
public class AppConfig {
}
```

```java
ApplicationContext context =
        new AnnotationConfigApplicationContext(AppConfig.class);

PaymentService paymentService =
        context.getBean(PaymentService.class);
```

In Spring Boot, you normally do not create the context manually.

```java
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

`SpringApplication.run(...)` creates and initializes the Spring application context.

---

# 4. IoC — Inversion of Control

IoC is closely related to Spring Beans and Dependency Injection.

Without Spring:

```java
PaymentService paymentService = new PaymentService();
```

Your application controls object creation.

With Spring:

```java
@Service
public class PaymentService {
}
```

Spring controls object creation.

That change in responsibility is called:

> **Inversion of Control**

The application says what dependencies it needs, while the Spring container decides how to create and provide them.

---

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

The Web area in Spring includes:

- Servlet
- Web
- Spring MVC
- WebSocket
- historically, Portlet

---

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

You do **not** need to learn every Spring module at the same depth.

For modern Spring Boot development, a useful priority is:

1. IoC and Dependency Injection
2. Spring Beans
3. `ApplicationContext`
4. Constructor Injection
5. Spring Boot Auto-Configuration
6. Spring MVC
7. REST Controllers
8. Service Layer
9. Repository Layer
10. JDBC
11. JPA and Hibernate
12. Spring Data JPA
13. Transactions
14. Spring Security
15. AOP
16. Spring Test
17. Messaging
18. WebSocket
19. OXM
20. Instrumentation
21. Portlet as legacy awareness only

---

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
