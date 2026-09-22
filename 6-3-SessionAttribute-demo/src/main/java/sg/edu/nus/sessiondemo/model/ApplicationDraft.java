package sg.edu.nus.sessiondemo.model;

/*
 * TEMPORARY MVC WORKFLOW OBJECT
 * -----------------------------
 * This class is deliberately NOT annotated with @Entity.
 *
 * It represents an incomplete job application while the applicant
 * moves through several pages:
 *
 *   Personal Details -> Education -> Experience -> Review
 *
 * JobApplicationController first places this object in Model:
 *
 *   model.addAttribute("applicationDraft", ...)
 *
 * The controller's class-level:
 *
 *   @SessionAttributes("applicationDraft")
 *
 * lets Spring MVC keep this same logical Model attribute available
 * between HTTP requests.
 *
 * Only after the final Submit step is the draft converted into the
 * persistent SubmittedApplication JPA entity.
 */
public class ApplicationDraft {

    private String fullName;
    private String email;
    private String highestEducation;
    private String currentRole;
    private Integer yearsOfExperience;

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
}
