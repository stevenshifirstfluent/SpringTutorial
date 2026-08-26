package sg.edu.nus.validation.tutorial;

import jakarta.validation.Valid;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;


public class User {


    private Long id;


    @NotBlank(
            message = "Username is required"
    )
    @Size(
            min = 3,
            max = 30,
            message = "Username must be 3-30 chars"
    )
    private String username;


    @NotBlank(
            message = "Email is required"
    )
    @Email(
            message =
                    "Must be a valid email address"
    )
    private String email;


    @NotBlank(
            message = "Password is required"
    )
    @Size(
            min = 8,
            message =
                    "Password must be at least 8 chars"
    )
    @Pattern(
            regexp =
                    "^(?=.*[A-Z])(?=.*\\d).+$",
            message =
                    "Password needs uppercase and digit"
    )
    private String password;


    @Min(
            value = 18,
            message = "Age must be at least 18"
    )
    @Max(
            value = 120,
            message = "Age must be at most 120"
    )
    private int age;


    @Past(
            message =
                    "Date of birth must be in the past"
    )
    private LocalDate dateOfBirth;


    /*
     * Custom ConstraintValidator example.
     */
    @NotBlank(
            message =
                    "Phone number is required"
    )
    @ValidPhone(
            regexp =
                    "^\\+?[0-9]{10,15}$",
            message =
                    "Phone must be 10-15 digits"
    )
    private String phoneNumber;


    /*
     * Cascading validation.
     *
     * Without @Valid, constraints declared
     * inside Address are not automatically
     * evaluated.
     */
    @Valid
    private Address address;


    public User() {
    }


    public Long getId() {

        return id;
    }


    public void setId(
            Long id) {

        this.id = id;
    }


    public String getUsername() {

        return username;
    }


    public void setUsername(
            String username) {

        this.username = username;
    }


    public String getEmail() {

        return email;
    }


    public void setEmail(
            String email) {

        this.email = email;
    }


    public String getPassword() {

        return password;
    }


    public void setPassword(
            String password) {

        this.password = password;
    }


    public int getAge() {

        return age;
    }


    public void setAge(
            int age) {

        this.age = age;
    }


    public LocalDate getDateOfBirth() {

        return dateOfBirth;
    }


    public void setDateOfBirth(
            LocalDate dateOfBirth) {

        this.dateOfBirth =
                dateOfBirth;
    }


    public String getPhoneNumber() {

        return phoneNumber;
    }


    public void setPhoneNumber(
            String phoneNumber) {

        this.phoneNumber =
                phoneNumber;
    }


    public Address getAddress() {

        return address;
    }


    public void setAddress(
            Address address) {

        this.address = address;
    }
}
