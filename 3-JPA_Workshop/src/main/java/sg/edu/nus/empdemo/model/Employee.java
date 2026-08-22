package sg.edu.nus.empdemo.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Employee is the owning side of Employee <-> Department
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    // Employee is the owning side of Employee <-> Project
    @ManyToMany(
        fetch = FetchType.LAZY,
        cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
        }
    )
    @JoinTable(
        name = "employee_projects",
        joinColumns = @JoinColumn(name = "employee_id"),
        inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private Set<Project> projects = new HashSet<>();

    // Course is the owning side.
    // Employee is the inverse side.
    @OneToMany(
        mappedBy = "employee",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Course> courses = new ArrayList<>();

    public Employee() {
    }

    public Employee(String name) {
        this.name = name;
    }

    public Employee(Long id, String name) {
        this.id = id;
        this.name = name;
    }

 // -----------------------------
    // Department helper method
    // -----------------------------

    public void assignDepartment(Department department) {

        if (this.department == department) {
            return;
        }

        if (this.department != null) {
            Department oldDepartment = this.department;
            this.department = null;

            if (oldDepartment.getEmployee() == this) {
                oldDepartment.setEmployee(null);
            }
        }

        this.department = department;

        if (department != null) {

            Employee oldEmployee = department.getEmployee();

            if (oldEmployee != null && oldEmployee != this) {
                oldEmployee.setDepartment(null);
            }

            department.setEmployee(this);
        }
    }

    // --------------------------------------------------
    // Project relationship helpers
    // --------------------------------------------------

    public void addProject(Project project) {

        if (project == null) {
            return;
        }

        projects.add(project);
        project.getEmployees().add(this);
    }

    public void joinProject(Project project) {
        addProject(project);
    }

    public void removeProject(Project project) {

        if (project == null) {
            return;
        }

        projects.remove(project);
        project.getEmployees().remove(this);
    }

    // --------------------------------------------------
    // Course relationship helpers
    // --------------------------------------------------

    public void addCourse(Course course) {

        if (course == null) {
            return;
        }

        courses.add(course);
        course.setEmployee(this);
    }

    public void enrollInCourse(Course course) {
        addCourse(course);
    }

    public void removeCourse(Course course) {

        if (course == null) {
            return;
        }

        courses.remove(course);

        if (course.getEmployee() == this) {
            course.setEmployee(null);
        }
    }

    // --------------------------------------------------
    // Getters and setters
    // --------------------------------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}