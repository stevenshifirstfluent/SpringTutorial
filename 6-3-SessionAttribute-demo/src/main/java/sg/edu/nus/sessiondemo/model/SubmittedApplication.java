package sg.edu.nus.sessiondemo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/*
 * DURABLE DATABASE ENTITY
 * -----------------------
 * Unlike ApplicationDraft, this class IS a JPA entity.
 *
 * It represents a completed job application that has passed the
 * Review step and has been explicitly submitted by the applicant.
 *
 * Teaching contrast:
 *
 * ApplicationDraft
 *   -> temporary multi-request MVC workflow data
 *   -> maintained using @SessionAttributes
 *   -> not saved to H2 during the wizard
 *
 * SubmittedApplication
 *   -> completed business record
 *   -> saved to H2 only at final Submit
 */
@Entity
@Table(name = "submitted_applications")
public class SubmittedApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    private String highestEducation;

    /*
     * The Java property can still be called currentRole, but we avoid
     * generating a database column named current_role.
     *
     * H2 recognises CURRENT_ROLE as a SQL keyword / built-in expression.
     * Using a different physical column name keeps the entity portable and
     * avoids relying on database-specific keyword workarounds.
     */
    @Column(name = "job_role")
    private String currentRole;
    private Integer yearsOfExperience;
    private LocalDateTime submittedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHighestEducation() {
        return highestEducation;
    }

    public void setHighestEducation(String highestEducation) {
        this.highestEducation = highestEducation;
    }

    public String getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}
