# JPA Workshop Testing Strategy

## 1. Purpose

This test suite verifies that the JPA solution developed in the workshop works correctly from the Java object model through to the database and repository layer.

The tests focus on three areas:

1. **Relationship behaviour**
2. **Persistence and cascade behaviour**
3. **Repository query behaviour**

The objective is not to test Spring Data JPA or Hibernate itself. Instead, the tests verify the code created in the workshop:

- Entity mappings
- Owning and inverse sides
- Bidirectional helper methods
- Cascade rules
- Orphan removal
- Derived query methods
- Custom JPQL queries
- `JOIN FETCH` queries

---

## 2. Test Case Design Principles

The test cases in this workshop are designed from the **requirements and business rules**, rather than from individual lines of code.

### 2.1 Requirement-driven testing

Each important requirement should have a corresponding test.

```text
Requirement
    |
    v
Entity mapping / repository method
    |
    v
Test case
```

Example:

```text
Requirement:
Find courses that start after a specified date
        |
        v
Repository method:
findByStartsAfter(LocalDate date)
        |
        v
Test:
shouldFindCoursesStartingAfterDate()
```

### 2.2 Test one clear behaviour at a time

Each test should answer one question.

Examples:

```text
Does assigning a Department update both sides?
Does deleting an Employee delete its Courses?
Does deleting an Employee preserve its Projects?
Does findByDateRange() return the expected Projects?
```

### 2.3 Use Arrange - Act - Assert

```text
Arrange
  |
  | Prepare the test data
  v
Act
  |
  | Execute the behaviour being tested
  v
Assert
  |
  | Verify the result
  v
Pass / Fail
```

Example:

```java
@Test
void shouldFindEmployeeByPartialNameIgnoringCase() {

    // Arrange
    Employee employee = new Employee("Alice Tan");

    entityManager.persistAndFlush(employee);
    entityManager.clear();

    // Act
    List<Employee> result =
            employeeRepository
                    .findByNameContainingIgnoreCase("alice");

    // Assert
    assertThat(result).hasSize(1);

    assertThat(result.get(0).getName())
            .isEqualTo("Alice Tan");
}
```

### 2.4 Test at the appropriate level

| Requirement type | Test approach |
|---|---|
| Java relationship helper | Test the object references directly |
| Cascade / orphan behaviour | Persist, flush, clear, then verify database state |
| Repository query | Prepare data with `TestEntityManager`, then execute the repository method |
| JPQL / `JOIN FETCH` | Persist data, clear the persistence context, then execute the custom query |

### 2.5 Test positive and important negative behaviour

```text
Delete Employee
     |
     +---- Courses should be deleted      PASS condition
     |
     +---- Projects should NOT be deleted PASS condition
```

### 2.6 Do not test framework behaviour unnecessarily

We test:

```text
Our entity mappings
Our relationship ownership
Our helper methods
Our cascade choices
Our repository methods
Our JPQL queries
Our JOIN FETCH queries
```

We do not need dedicated tests for:

```text
JpaRepository.save()
JpaRepository.findAll()
Java getters and setters
Hibernate proxy implementation
Spring Data internal SQL generation
```

---

## 3. Testing Strategy Overview

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

Repository-test workflow:

```text
             ARRANGE
                |
                v
       TestEntityManager
     persist / persistAndFlush
                |
                v
       Database test data
                |
                v
      entityManager.clear()
                |
                v
              ACT
                |
                v
       Repository method
                |
                v
             ASSERT
                |
                v
        Expected result
```

Responsibility separation:

```text
TestEntityManager
        |
        +---- Prepare the database state

Repository under test
        |
        +---- Execute the behaviour being tested

AssertJ
        |
        +---- Verify the result
```

---

## 4. Test Class Structure

```text
src/test/java
└── sg/edu/nus/empdemo/repository
    ├── EmployeeRepositoryTest.java
    ├── DepartmentRepositoryTest.java
    ├── ProjectRepositoryTest.java
    └── CourseRepositoryTest.java
```

---

## 5. Why `@DataJpaTest` Is Used

Use:

```java
@DataJpaTest
class EmployeeRepositoryTest {
}
```

`@DataJpaTest` loads the components needed for JPA testing, including:

```text
Entities
Repositories
Hibernate
DataSource
TestEntityManager
```

It does not start the full Spring Boot application.

> **Important:** The exact import package for `DataJpaTest` depends on the Spring Boot version used by the workshop project.

For Spring Boot 3.x, the commonly used import is:

```java
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
```

Use the equivalent package supplied by the Spring Boot version in the actual workshop project.

---

## 6. Why `TestEntityManager` Is Used

The workshop uses `TestEntityManager` to prepare database data for repository tests.

```java
@Autowired
private TestEntityManager entityManager;
```

For Spring Boot 3.x, the commonly used import is:

```java
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
```

Use the equivalent import supplied by the Spring Boot version used by the project.

For repository tests, the intended separation is:

```text
Preparing data
     |
     v
TestEntityManager

Testing query
     |
     v
Repository
```

Example:

```java
Employee employee = new Employee("Alice Tan");

entityManager.persistAndFlush(employee);
entityManager.clear();

List<Employee> result =
        employeeRepository
                .findByNameContainingIgnoreCase("alice");
```

---

## 7. `persist()`, `flush()`, `persistAndFlush()` and `clear()`

### 7.1 `persist()`

```java
entityManager.persist(entity);
```

Makes an entity managed and schedules it to be inserted.

```java
entityManager.persist(project1);
entityManager.persist(project2);
entityManager.persist(project3);

entityManager.flush();
```

### 7.2 `persistAndFlush()`

```java
entityManager.persistAndFlush(entity);
```

Persists the entity and immediately flushes pending changes to the database.

### 7.3 `flush()`

```java
entityManager.flush();
```

Forces pending SQL changes to be sent to the database.

### 7.4 `clear()`

```java
entityManager.clear();
```

Clears the current persistence context.

```text
Java Entity
    |
    v
Persistence Context
    |
    | persist / persistAndFlush
    v
Database
    |
    | clear()
    v
Empty Persistence Context
    |
    v
Repository Query
    |
    v
Read data again
```

---

## 8. Relationship Synchronization Tests

The workshop model contains:

```text
Department  1 <------> 1  Employee
                           |
                           +------ M <------> M  Project
                           |
                           +------ 1 <------> M  Course
```

### 8.1 Employee - Department

Employee is the owning side:

```java
@OneToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "department_id")
private Department department;
```

Department is the inverse side:

```java
@OneToOne(
    mappedBy = "department",
    fetch = FetchType.LAZY
)
private Employee employee;
```

Test:

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

### 8.2 Employee - Project

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

### 8.3 Employee - Course

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

## 9. Cascade and Persistence Tests

### 9.1 Course Cascade Persist

```java
@Test
void shouldCascadePersistCoursesWhenEmployeeIsPersisted() {

    Employee employee = new Employee("Charlie Wong");

    Course course = new Course(
            "Hibernate",
            2.0,
            LocalDate.of(2026, 6, 1));

    employee.addCourse(course);

    entityManager.persistAndFlush(employee);

    Long employeeId = employee.getId();

    entityManager.clear();

    Optional<Employee> result =
            employeeRepository
                    .findByIdWithCourses(employeeId);

    assertThat(result).isPresent();
    assertThat(result.get().getCourses())
            .hasSize(1);
}
```

### 9.2 Employee Deletion Deletes Courses

```text
Employee
   |
   +---- Course A
   |
   +---- Course B

Delete Employee
      |
      v
Course A deleted
Course B deleted
```

```java
@Test
void deletingEmployeeShouldDeleteCourses() {

    Employee employee = new Employee("Alice");

    Course course = new Course(
            "Spring Boot",
            2.0,
            LocalDate.of(2026, 9, 1));

    employee.addCourse(course);

    entityManager.persistAndFlush(employee);

    Long employeeId = employee.getId();
    Long courseId = course.getId();

    entityManager.clear();

    Employee persistedEmployee =
            entityManager.find(
                    Employee.class,
                    employeeId);

    entityManager.remove(persistedEmployee);
    entityManager.flush();
    entityManager.clear();

    assertThat(courseRepository.findById(courseId))
            .isEmpty();
}
```

### 9.3 Orphan Removal

```text
Employee ---- Course

employee.removeCourse(course)
        |
        v
Course is no longer associated
        |
        v
orphanRemoval = true
        |
        v
Course record deleted
```

```java
@Test
void shouldDeleteCourseWhenRemovedFromEmployee() {

    Employee employee = new Employee("David Tan");

    Course course = new Course(
            "Java Fundamentals",
            1.0,
            LocalDate.of(2026, 7, 1));

    employee.addCourse(course);

    entityManager.persistAndFlush(employee);

    Long employeeId = employee.getId();
    Long courseId = course.getId();

    entityManager.clear();

    Employee persistedEmployee =
            entityManager.find(
                    Employee.class,
                    employeeId);

    Course persistedCourse =
            persistedEmployee.getCourses().get(0);

    persistedEmployee.removeCourse(persistedCourse);

    entityManager.flush();
    entityManager.clear();

    assertThat(courseRepository.findById(courseId))
            .isEmpty();
}
```

### 9.4 Project Must Survive Employee Deletion

```text
Alice -----+
           |
           +---- AI Platform
           |
Bob -------+
```

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

    entityManager.persistAndFlush(employee);

    Long employeeId = employee.getId();
    Long projectId = project.getId();

    entityManager.clear();

    Employee persistedEmployee =
            entityManager.find(
                    Employee.class,
                    employeeId);

    entityManager.remove(persistedEmployee);
    entityManager.flush();
    entityManager.clear();

    assertThat(projectRepository.findById(projectId))
            .isPresent();
}
```

---

## 10. Derived Query Method Tests

Examples from the workshop:

```java
List<Course> findByNameContainingIgnoreCase(String name);
```

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

Repository-query pattern:

```text
TestEntityManager
      |
      | Arrange database records
      v
entityManager.clear()
      |
      v
Repository method
      |
      | Act
      v
Assert result
```

Example:

```java
@Test
void shouldFindCoursesStartingAfterDate() {

    Course course1 = new Course(
            "Spring Boot",
            2.0,
            LocalDate.of(2026, 3, 1));

    Course course2 = new Course(
            "Spring Data JPA",
            1.5,
            LocalDate.of(2026, 5, 1));

    entityManager.persist(course1);
    entityManager.persist(course2);

    entityManager.flush();
    entityManager.clear();

    List<Course> result =
            courseRepository.findByStartsAfter(
                    LocalDate.of(2026, 3, 15));

    assertThat(result).hasSize(1);

    assertThat(result.get(0).getName())
            .isEqualTo("Spring Data JPA");
}
```

---

## 11. Custom JPQL Tests

Example repository method:

```java
@Query("""
    SELECT p
    FROM Project p
    WHERE p.startDate >= :startDate
      AND p.endDate <= :endDate
    """)
List<Project> findByDateRange(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);
```

Test:

```java
@Test
void shouldFindProjectsWithinDateRange() {

    Project project1 = new Project(
            "AI Platform",
            "AI",
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 31));

    Project project2 = new Project(
            "Web Portal",
            "Web",
            LocalDate.of(2026, 3, 1),
            LocalDate.of(2026, 9, 30));

    Project project3 = new Project(
            "Legacy Migration",
            "Legacy",
            LocalDate.of(2025, 1, 1),
            LocalDate.of(2025, 12, 31));

    entityManager.persist(project1);
    entityManager.persist(project2);
    entityManager.persist(project3);

    entityManager.flush();
    entityManager.clear();

    List<Project> result =
            projectRepository.findByDateRange(
                    LocalDate.of(2026, 1, 1),
                    LocalDate.of(2026, 12, 31));

    assertThat(result)
            .extracting(Project::getName)
            .containsExactlyInAnyOrder(
                    "AI Platform",
                    "Web Portal");
}
```

---

## 12. `JOIN FETCH` Tests

All workshop associations use:

```java
FetchType.LAZY
```

When related data is required, the repositories use `JOIN FETCH`.

Example:

```java
@Query("""
    SELECT e
    FROM Employee e
    LEFT JOIN FETCH e.department
    WHERE e.id = :id
    """)
Optional<Employee> findByIdWithDepartment(
        @Param("id") Long id);
```

Test:

```java
@Test
void shouldFetchEmployeeWithDepartment() {

    Department department =
            new Department("Information Technology");

    entityManager.persistAndFlush(department);

    Employee employee =
            new Employee("Alice Tan");

    employee.assignDepartment(department);

    entityManager.persistAndFlush(employee);

    Long employeeId = employee.getId();

    entityManager.clear();

    Optional<Employee> result =
            employeeRepository
                    .findByIdWithDepartment(employeeId);

    assertThat(result).isPresent();

    assertThat(result.get().getDepartment())
            .isNotNull();

    assertThat(result.get()
            .getDepartment()
            .getName())
            .isEqualTo("Information Technology");
}
```

The same strategy applies to:

```text
EmployeeRepository
    findByIdWithDepartment
    findByIdWithProjects
    findByIdWithCourses

DepartmentRepository
    findByIdWithEmployee

ProjectRepository
    findByIdWithEmployees

CourseRepository
    findByIdWithEmployee
```

---

## 13. Repository Requirement Coverage

### 13.1 EmployeeRepository

| Workshop requirement | Test |
|---|---|
| Find by partial name, ignoring case | `shouldFindEmployeeByPartialNameIgnoringCase()` |
| Fetch Employee and Department | `shouldFetchEmployeeWithDepartment()` |
| Find Employees by `departmentId` | `shouldFindEmployeeByDepartmentId()` |
| Fetch Employee and Projects | `shouldFetchEmployeeWithProjects()` |
| Find Employees by `projectId` | `shouldFindEmployeesByProjectId()` |
| Fetch Employee and Courses | `shouldFetchEmployeeWithCourses()` |

### 13.2 DepartmentRepository

| Workshop requirement | Test |
|---|---|
| Find by exact name | `shouldFindDepartmentByExactName()` |
| Find by partial name, ignoring case | `shouldFindDepartmentByPartialNameIgnoringCase()` |
| Fetch Department and Employee | `shouldFetchDepartmentWithEmployee()` |
| Check Department has Employee | `shouldReturnTrueWhenDepartmentHasEmployee()` |
| Check Department has no Employee | `shouldReturnFalseWhenDepartmentHasNoEmployee()` |

### 13.3 ProjectRepository

| Workshop requirement | Test |
|---|---|
| Find by exact name | `shouldFindProjectByExactName()` |
| Find by partial name, ignoring case | `shouldFindProjectByPartialNameIgnoringCase()` |
| Find Projects ending after date | `shouldFindProjectsEndingAfterDate()` |
| Find Projects within date range | `shouldFindProjectsWithinDateRange()` |
| Fetch Project and Employees | `shouldFetchProjectWithEmployees()` |
| Find Projects by `employeeId` | `shouldFindProjectsByEmployeeId()` |

### 13.4 CourseRepository

| Workshop requirement | Test |
|---|---|
| Find by partial name, ignoring case | `shouldFindCourseByPartialNameIgnoringCase()` |
| Find Courses starting after date | `shouldFindCoursesStartingAfterDate()` |
| Find Courses by maximum duration | `shouldFindCoursesByMaximumDuration()` |
| Find Courses by `employeeId` | `shouldFindCoursesByEmployeeId()` |
| Fetch Course and Employee | `shouldFetchCourseWithEmployee()` |
| Find by `employeeId` and start date | `shouldFindCoursesByEmployeeAndStartDate()` |

---

## 14. Mapping and Cascade Coverage

| Workshop requirement | Verification |
|---|---|
| Employee - Department bidirectional 1:1 | `assignDepartmentShouldSynchronizeBothSides()` |
| Employee - Project bidirectional M:M | `addProjectShouldSynchronizeBothSides()` |
| Remove Employee - Project relationship | `removeProjectShouldSynchronizeBothSides()` |
| Employee - Course bidirectional 1:M | `addCourseShouldSynchronizeBothSides()` |
| Remove Employee - Course relationship | `removeCourseShouldSynchronizeBothSides()` |
| Course cascade persist | `shouldCascadePersistCoursesWhenEmployeeIsPersisted()` |
| Delete Employee -> delete Courses | `deletingEmployeeShouldDeleteCourses()` |
| Course orphan removal | `shouldDeleteCourseWhenRemovedFromEmployee()` |
| Delete Employee -> preserve Project | `shouldNotDeleteProjectWhenEmployeeIsDeleted()` |

---

## 15. Why Tests Are Transactional

`@DataJpaTest` tests normally run inside a transaction.

```text
Start Test
    |
    v
Begin Transaction
    |
    v
Persist Test Data
    |
    v
Execute Repository Query
    |
    v
Assertions
    |
    v
Rollback
    |
    v
Database ready for next test
```

This helps keep tests independent.

---

## 16. Running the Tests

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

Or use Maven:

```bash
mvn test
```

Expected result:

```text
EmployeeRepositoryTest      PASS
DepartmentRepositoryTest    PASS
ProjectRepositoryTest       PASS
CourseRepositoryTest        PASS
```

---

## 17. Final Testing Strategy

```text
                         Workshop Requirement
                                  |
             +--------------------+--------------------+
             |                    |                    |
             v                    v                    v
      Relationship Rule     Persistence Rule       Query Rule
             |                    |                    |
             v                    v                    v
      Java helper test     TestEntityManager      TestEntityManager
                                  |                    |
                                  v                    v
                           Persist / Flush        Prepare test data
                                  |                    |
                                  v                    v
                               Clear                 Clear
                                  |                    |
                                  v                    v
                           Verify database       Repository query
                                  |                    |
                                  +---------+----------+
                                            |
                                            v
                                          Assert
```

The key principle is:

> **Each test should prove one explicit workshop requirement or business rule at the most appropriate level.**

For repository integration tests:

> **Use `TestEntityManager` to arrange the persisted database state, clear the persistence context, then use the repository under test to perform the query being verified.**

For relationship helper methods:

> **Test the Java object state directly because no database interaction is required to prove in-memory synchronization.**

This keeps the workshop tests focused, traceable to the requirements, and consistent with the use of `@DataJpaTest` and `TestEntityManager`.
