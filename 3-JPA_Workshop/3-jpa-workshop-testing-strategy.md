# JPA Workshop Testing Strategy

## 1. Purpose

The purpose of the test suite is to verify that the JPA implementation works correctly at three levels:

1. **Java object relationship behaviour**
2. **JPA persistence and cascade behaviour**
3. **Spring Data JPA repository query behaviour**

The tests are designed to validate the code written in this workshop, including:

- Entity relationships
- Owning and inverse sides
- Bidirectional helper methods
- Cascade behaviour
- Orphan removal
- Derived query methods
- Custom JPQL queries
- `JOIN FETCH` queries

The tests are **not** intended to test Spring Data JPA or Hibernate itself.

---

## 2. Testing Strategy Overview

```text
                         JPA WORKSHOP TESTING
                                  |
              +-------------------+-------------------+
              |                   |                   |
              v                   v                   v
      Relationship Tests   Persistence Tests    Repository Tests
              |                   |                   |
      +-------+-------+      +----+-----+       +-----+------+
      |       |       |      |          |       |            |
      v       v       v      v          v       v            v
     1:1     1:M     M:M   Cascade   Orphan   Derived     JPQL /
                            Behaviour Removal   Queries     JOIN FETCH
```

The testing flow can also be understood as:

```text
Create Java objects
        |
        v
Connect entities using helper methods
        |
        v
Save using JpaRepository
        |
        v
Flush changes to the database
        |
        v
Clear the persistence context
        |
        v
Query the database again
        |
        v
Assert the expected result
```

---

## 3. Test Class Structure

The workshop contains one repository test class for each repository.

```text
src/test/java
└── sg/edu/nus/empdemo/repository
    ├── EmployeeRepositoryTest.java
    ├── DepartmentRepositoryTest.java
    ├── ProjectRepositoryTest.java
    └── CourseRepositoryTest.java
```

Each test class focuses on the behaviour related to its repository while also verifying the related entity mappings.

---

# 4. Why `@DataJpaTest` Is Used

The workshop tests focus only on the JPA persistence layer.

For that reason, use:

```java
@DataJpaTest
class EmployeeRepositoryTest {
}
```

`@DataJpaTest` loads the JPA-related parts of the application, such as:

```text
Entities
Repositories
Hibernate
DataSource
EntityManager
```

It does not load the entire Spring Boot application.

This makes the tests:

- Faster
- More focused
- Easier to troubleshoot

> **Important:** The package used to import `DataJpaTest` depends on the Spring Boot version used by the project.

For Spring Boot 3.x:

```java
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
```

For newer Spring Boot versions where the package has moved, use the `DataJpaTest` import available in that version.

---

# 5. Test Database

For this workshop, an embedded H2 database can be used.

Add the following dependency:

```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```

Also ensure Spring Boot testing support is available:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

This allows the repository tests to run without requiring students to install an external database.

---

# 6. Relationship Synchronization Tests

The first group of tests verifies the bidirectional helper methods.

The workshop contains three relationships:

```text
Department  1 <------> 1  Employee
                           |
                           +------ M <------> M  Project
                           |
                           +------ 1 <------> M  Course
```

A bidirectional relationship has two Java references.

For example:

```text
Employee.department
        ^
        |
        v
Department.employee
```

Both sides should remain synchronized.

---

## 6.1 Employee and Department

The relationship is created through:

```java
employee.assignDepartment(department);
```

The test verifies both references:

```java
@Test
void assignDepartmentShouldSynchronizeBothSides() {

    Employee employee = new Employee("Alice");
    Department department = new Department("IT");

    employee.assignDepartment(department);

    assertThat(employee.getDepartment())
            .isEqualTo(department);

    assertThat(department.getEmployee())
            .isEqualTo(employee);
}
```

This confirms:

```text
Employee.department  ---> Department
Department.employee  ---> Employee
```

---

## 6.2 Employee and Project

The many-to-many relationship uses:

```java
employee.addProject(project);
```

The corresponding test verifies both collections:

```java
@Test
void addProjectShouldSynchronizeBothSides() {

    Employee employee = new Employee("Alice");

    Project project = new Project(
            "AI Project",
            "Test project",
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 31));

    employee.addProject(project);

    assertThat(employee.getProjects())
            .contains(project);

    assertThat(project.getEmployees())
            .contains(employee);
}
```

The removal operation should also synchronize both sides:

```java
@Test
void removeProjectShouldSynchronizeBothSides() {

    Employee employee = new Employee("Alice");

    Project project = new Project(
            "AI Project",
            "Test project",
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 31));

    employee.addProject(project);
    employee.removeProject(project);

    assertThat(employee.getProjects())
            .doesNotContain(project);

    assertThat(project.getEmployees())
            .doesNotContain(employee);
}
```

---

## 6.3 Employee and Course

The one-to-many relationship uses:

```java
employee.addCourse(course);
```

The test verifies:

```java
@Test
void addCourseShouldSynchronizeBothSides() {

    Employee employee = new Employee("Alice");

    Course course = new Course(
            "Spring Boot",
            2.0,
            LocalDate.of(2026, 9, 1));

    employee.addCourse(course);

    assertThat(employee.getCourses())
            .contains(course);

    assertThat(course.getEmployee())
            .isEqualTo(employee);
}
```

Removing the relationship should also clear both sides:

```java
@Test
void removeCourseShouldSynchronizeBothSides() {

    Employee employee = new Employee("Alice");

    Course course = new Course(
            "Spring Boot",
            2.0,
            LocalDate.of(2026, 9, 1));

    employee.addCourse(course);
    employee.removeCourse(course);

    assertThat(employee.getCourses())
            .doesNotContain(course);

    assertThat(course.getEmployee())
            .isNull();
}
```

---

# 7. Persistence and Cascade Tests

The next group verifies whether JPA persists and removes related records according to the cascade configuration.

The Employee-Course relationship is configured as:

```java
@OneToMany(
    mappedBy = "employee",
    fetch = FetchType.LAZY,
    cascade = CascadeType.ALL,
    orphanRemoval = true
)
private List<Course> courses = new ArrayList<>();
```

This produces three important behaviours to test.

---

## 7.1 Cascade Persist

Saving an Employee should also persist its new Courses.

```java
@Test
void shouldCascadePersistCoursesWhenEmployeeIsSaved() {

    Employee employee = new Employee("Charlie Wong");

    Course course = new Course(
            "Hibernate",
            2.0,
            LocalDate.of(2026, 6, 1));

    employee.addCourse(course);

    Employee saved =
            employeeRepository.saveAndFlush(employee);

    entityManager.clear();

    Optional<Employee> result =
            employeeRepository.findByIdWithCourses(
                    saved.getId());

    assertThat(result).isPresent();
    assertThat(result.get().getCourses()).hasSize(1);

    assertThat(result.get().getCourses().get(0).getName())
            .isEqualTo("Hibernate");
}
```

This validates the `PERSIST` behaviour contained in:

```java
CascadeType.ALL
```

---

## 7.2 Employee Deletion Cascades to Courses

The workshop explicitly requires that deleting an Employee also deletes the Employee's Courses.

```java
@Test
void deletingEmployeeShouldDeleteCourses() {

    Employee employee = new Employee("Alice");

    Course course = new Course(
            "Spring Boot",
            2.0,
            LocalDate.of(2026, 9, 1));

    employee.addCourse(course);

    employeeRepository.saveAndFlush(employee);

    Long courseId = course.getId();

    employeeRepository.delete(employee);
    employeeRepository.flush();

    entityManager.clear();

    assertThat(courseRepository.findById(courseId))
            .isEmpty();
}
```

The expected behaviour is:

```text
Delete Employee
      |
      v
Cascade REMOVE
      |
      v
Delete associated Courses
```

---

## 7.3 Orphan Removal

`orphanRemoval = true` means that when a Course is removed from the Employee's `courses` collection, that Course should also be deleted from the database.

```java
@Test
void shouldDeleteCourseWhenRemovedFromEmployee() {

    Employee employee = new Employee("David Tan");

    Course course = new Course(
            "Java Fundamentals",
            1.0,
            LocalDate.of(2026, 7, 1));

    employee.addCourse(course);

    employeeRepository.saveAndFlush(employee);

    Long courseId = course.getId();

    employee.removeCourse(course);

    employeeRepository.saveAndFlush(employee);

    entityManager.clear();

    assertThat(courseRepository.findById(courseId))
            .isEmpty();
}
```

The expected behaviour is:

```text
Employee
   |
   +---- Course

removeCourse(course)
   |
   v

Employee

Course becomes an orphan
   |
   v

Course record is deleted
```

---

# 8. Project Lifetime Test

The Employee-Project relationship intentionally uses:

```java
cascade = {
    CascadeType.PERSIST,
    CascadeType.MERGE
}
```

It does **not** use:

```java
CascadeType.ALL
```

A Project may be shared by multiple Employees.

```text
Alice ----+
          |
          +---- AI Platform
          |
Bob ------+
```

Deleting Alice must not delete the shared Project.

```java
@Test
void shouldNotDeleteProjectWhenEmployeeIsDeleted() {

    Employee employee = new Employee("Eric Lee");

    Project project = new Project(
            "Shared Platform",
            "Shared enterprise project",
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 31));

    employee.addProject(project);

    employeeRepository.saveAndFlush(employee);

    Long projectId = project.getId();

    employeeRepository.delete(employee);
    employeeRepository.flush();

    entityManager.clear();

    Optional<Project> result =
            projectRepository.findById(projectId);

    assertThat(result).isPresent();
}
```

This test validates both the JPA cascade choice and the intended business behaviour.

---

# 9. Derived Query Method Tests

Spring Data JPA can generate queries from repository method names.

For example:

```java
List<Employee> findByNameContainingIgnoreCase(String name);
```

Other workshop examples include:

```java
List<Course> findByStartsAfter(LocalDate date);
```

```java
List<Employee> findByDepartmentId(Long departmentId);
```

```java
List<Employee> findByProjectsId(Long projectId);
```

```java
List<Course> findByEmployeeIdAndStartsAfter(
        Long employeeId,
        LocalDate date);
```

The tests verify:

1. The repository method name is valid.
2. Spring Data can derive the query.
3. The returned data matches the expected result.

Example:

```java
@Test
void shouldFindEmployeeByPartialNameIgnoringCase() {

    List<Employee> employees =
            employeeRepository
                    .findByNameContainingIgnoreCase("alice");

    assertThat(employees).hasSize(1);

    assertThat(employees.get(0).getName())
            .isEqualTo("Alice Tan");
}
```

---

# 10. Custom JPQL Query Tests

Some requirements use explicitly written JPQL.

For example:

````java
@Query("""
    SELECT p
    FROM Project p
    WHERE p.startDate >= :startDate
      AND p.endDate <= :endDate
    """)
List<Project> findByDateRange(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);
````

The test verifies:

- JPQL syntax
- Parameter binding
- Query conditions
- Returned results

Example:

```java
@Test
void shouldFindProjectsWithinDateRange() {

    List<Project> projects =
            projectRepository.findByDateRange(
                    LocalDate.of(2026, 1, 1),
                    LocalDate.of(2026, 12, 31));

    assertThat(projects)
            .extracting(Project::getName)
            .containsExactlyInAnyOrder(
                    "AI Platform",
                    "Web Portal");
}
```

---

# 11. `JOIN FETCH` Tests

All workshop associations use:

```java
FetchType.LAZY
```

When a query explicitly needs related entities, the repository uses `JOIN FETCH`.

For example:

````java
@Query("""
    SELECT e
    FROM Employee e
    LEFT JOIN FETCH e.department
    WHERE e.id = :id
    """)
Optional<Employee> findByIdWithDepartment(
        @Param("id") Long id);
````

The test then confirms that the related entity is available:

```java
@Test
void shouldFetchEmployeeWithDepartment() {

    Optional<Employee> result =
            employeeRepository.findByIdWithDepartment(
                    alice.getId());

    assertThat(result).isPresent();

    Employee employee = result.get();

    assertThat(employee.getDepartment())
            .isNotNull();

    assertThat(employee.getDepartment().getName())
            .isEqualTo("Information Technology");
}
```

The same strategy is used for:

```text
findByIdWithDepartment
findByIdWithProjects
findByIdWithCourses
findByIdWithEmployee
findByIdWithEmployees
```

The purpose is to verify that the explicit fetch queries required by the workshop work correctly.

---

# 12. Why `flush()` and `EntityManager.clear()` Are Important

A typical test contains:

```java
employeeRepository.saveAndFlush(employee);

entityManager.clear();
```

These two operations serve different purposes.

## `flush()`

```java
employeeRepository.flush();
```

or:

```java
employeeRepository.saveAndFlush(employee);
```

forces pending changes to be written to the database.

## `EntityManager.clear()`

```java
entityManager.clear();
```

removes managed entities from Hibernate's first-level cache.

Without clearing the persistence context, a query may return an already-managed Java object instead of demonstrating that the data can be loaded correctly from the database.

The intended flow is:

```text
Java Entity
    |
    v
Hibernate Persistence Context
    |
    | flush()
    v
Database
    |
    | clear()
    v
Persistence Context is empty
    |
    v
Run repository query
    |
    v
Load data from database
```

This gives stronger confidence that the JPA mapping is correct.

---

# 13. Repository Test Coverage

## EmployeeRepository

| Requirement | Test |
|---|---|
| Partial name ignoring case | `shouldFindEmployeeByPartialNameIgnoringCase` |
| Employee + Department | `shouldFetchEmployeeWithDepartment` |
| Employees by Department ID | `shouldFindEmployeeByDepartmentId` |
| Employee + Projects | `shouldFetchEmployeeWithProjects` |
| Employees by Project ID | `shouldFindEmployeesByProjectId` |
| Employee + Courses | `shouldFetchEmployeeWithCourses` |

## DepartmentRepository

| Requirement | Test |
|---|---|
| Exact name | `shouldFindDepartmentByExactName` |
| Partial name ignoring case | `shouldFindDepartmentByPartialNameIgnoringCase` |
| Department + Employee | `shouldFetchDepartmentWithEmployee` |
| Department has Employee | `shouldReturnTrueWhenDepartmentHasEmployee` |
| Department has no Employee | `shouldReturnFalseWhenDepartmentHasNoEmployee` |

## ProjectRepository

| Requirement | Test |
|---|---|
| Exact project name | `shouldFindProjectByExactName` |
| Partial name ignoring case | `shouldFindProjectByPartialNameIgnoringCase` |
| Ending after date | `shouldFindProjectsEndingAfterDate` |
| Date range | `shouldFindProjectsWithinDateRange` |
| Project + Employees | `shouldFetchProjectWithEmployees` |
| Projects by Employee ID | `shouldFindProjectsByEmployeeId` |

## CourseRepository

| Requirement | Test |
|---|---|
| Partial name ignoring case | `shouldFindCourseByPartialNameIgnoringCase` |
| Starts after date | `shouldFindCoursesStartingAfterDate` |
| Maximum duration | `shouldFindCoursesByMaximumDuration` |
| Courses by Employee ID | `shouldFindCoursesByEmployeeId` |
| Course + Employee | `shouldFetchCourseWithEmployee` |
| Employee ID + start date | `shouldFindCoursesByEmployeeAndStartDate` |

---

# 14. What Is Deliberately Not Tested

The workshop should avoid testing framework implementation details.

Examples that do **not** need dedicated tests include:

```text
Getters and setters
JpaRepository.save()
JpaRepository.findAll()
Hibernate proxy implementation
Spring Data's internal SQL generation
```

These behaviours are already tested by the frameworks.

The workshop should focus on code created by the participant:

```text
Entity mappings
Relationship ownership
Cascade configuration
Bidirectional helper methods
Derived query methods
Custom JPQL
JOIN FETCH queries
```

---

# 15. Running the Tests

In Spring Tool Suite (STS):

```text
Right-click the project
        |
        v
Run As
        |
        v
JUnit Test
```

Or run the test suite with Maven:

```bash
mvn test
```

A successful workshop should result in all repository tests passing:

```text
EmployeeRepositoryTest      PASS
DepartmentRepositoryTest    PASS
ProjectRepositoryTest       PASS
CourseRepositoryTest        PASS
```

---

# 16. Final Testing Strategy Summary

```text
                 JPA Workshop Tests
                         |
       +-----------------+------------------+
       |                                    |
       v                                    v
 Object Relationship                  Database Behaviour
       |                                    |
       |                          +---------+---------+
       |                          |                   |
       v                          v                   v
 Helper Methods                Cascade            Queries
       |                          |                   |
  +----+----+               +-----+-----+       +----+-----+
  |    |    |               |           |       |          |
  v    v    v               v           v       v          v
 1:1  1:M  M:M          Persist      Remove   Derived     JPQL
                                   / Orphan               JOIN FETCH
```

The key idea is:

> **A good JPA test does more than check whether a repository method returns data. It verifies that Java object relationships, JPA persistence behaviour, cascade rules, and repository queries all work together correctly.**

This testing strategy validates the complete JPA workflow covered by the workshop:

```text
Entity Design
     |
     v
Relationship Mapping
     |
     v
Persistence Behaviour
     |
     v
Repository Querying
     |
     v
Integration Verification
```
