package sg.edu.nus.empdemo.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double durationInMonths;

    private LocalDate starts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public Course() {
    }

    public Course(
            String name,
            Double durationInMonths,
            LocalDate starts) {

        this.name = name;
        this.durationInMonths = durationInMonths;
        this.starts = starts;
    }

    public Course(
            Long id,
            String name,
            Double durationInMonths,
            LocalDate starts) {

        this.id = id;
        this.name = name;
        this.durationInMonths = durationInMonths;
        this.starts = starts;
    }

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

    public Double getDurationInMonths() {
        return durationInMonths;
    }

    public void setDurationInMonths(Double durationInMonths) {
        this.durationInMonths = durationInMonths;
    }

    public LocalDate getStarts() {
        return starts;
    }

    public void setStarts(LocalDate starts) {
        this.starts = starts;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}