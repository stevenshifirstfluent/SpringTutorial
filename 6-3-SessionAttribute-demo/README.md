# Spring MVC Session Demo

This project demonstrates the difference between:

- class-level `@SessionAttributes`
- method-parameter `@SessionAttribute`

using one Job Application Wizard.

## Teaching scenario

### Existing session context: `preferredLanguage`

Before the job application starts, the user chooses a preferred language.
`LanguagePreferenceController` stores it explicitly:

```java
session.setAttribute("preferredLanguage", preferredLanguage);
```

Later, methods in `JobApplicationController` read that already-existing value:

```java
@SessionAttribute("preferredLanguage")
String preferredLanguage
```

`@SessionAttribute` does not create or store the value. It reads an existing
HTTP session attribute into a controller method parameter.

### Temporary MVC workflow state: `applicationDraft`

The job application spans several HTTP requests:

1. Personal Details
2. Education
3. Work Experience
4. Review

The controller declares:

```java
@SessionAttributes("applicationDraft")
```

The workflow starts with:

```java
model.addAttribute("applicationDraft", new ApplicationDraft());
```

There is deliberately no `session.setAttribute("applicationDraft", ...)` in
the business workflow. Spring MVC keeps that Model attribute between requests
because of class-level `@SessionAttributes`.

Only final Submit creates a durable JPA entity and writes it to H2. Then:

```java
sessionStatus.setComplete();
```

ends the controller-managed workflow state. `preferredLanguage` remains in the
HTTP session.

## UI design for teaching

Every workflow page uses the same information order:

1. Step indicator above the title
2. **What to observe** immediately below the title
3. Form and action button
4. **Current Session State** below the action

The bottom panel always shows both teaching values:

- `preferredLanguage` — existing session data read by `@SessionAttribute`
- `applicationDraft` — Model workflow data maintained by `@SessionAttributes`

This makes it easy to observe the draft growing across requests while the
language preference stays unchanged.

## Recommended classroom sequence

1. Open `/preferences`.
2. Store `English` as the preferred language.
3. Verify at the bottom of the page:
   - `preferredLanguage = English`
   - `applicationDraft = NOT PRESENT`
4. Start the application.
5. On Personal Details, verify that `applicationDraft` now exists.
6. Enter name and email and continue.
7. On Education, inspect the bottom panel and verify that name/email survived a new request.
8. Continue through Work Experience.
9. On Review, compare both session mechanisms side-by-side.
10. Submit the application.
11. On Success, verify:
    - `preferredLanguage` is still present
    - `applicationDraft` is not present
    - a database record now exists

## Run

Use Java 21 and Maven:

```bash
mvn spring-boot:run
```

Then open:

```text
http://localhost:8080
```

Useful pages:

```text
/preferences
/application/start
/session
/submitted-applications
/h2-console
```

H2 settings:

```text
JDBC URL: jdbc:h2:mem:jobapplicationdb
User: sa
Password: <blank>
```
