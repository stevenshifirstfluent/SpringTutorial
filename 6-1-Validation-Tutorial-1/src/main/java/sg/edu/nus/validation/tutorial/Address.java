package sg.edu.nus.validation.tutorial;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Address {

    @NotBlank(
            message = "Street is required"
    )
    private String street;


    @NotBlank(
            message = "City is required"
    )
    @Size(max = 100)
    private String city;


    @NotBlank(
            message = "Postal code required"
    )
    @Pattern(
            regexp = "\\d{5,6}",
            message = "Invalid postal code"
    )
    private String postalCode;


    @NotBlank(
            message = "Country required"
    )
    private String country;


    public Address() {
    }


    public String getStreet() {

        return street;
    }


    public void setStreet(
            String street) {

        this.street = street;
    }


    public String getCity() {

        return city;
    }


    public void setCity(
            String city) {

        this.city = city;
    }


    public String getPostalCode() {

        return postalCode;
    }


    public void setPostalCode(
            String postalCode) {

        this.postalCode = postalCode;
    }


    public String getCountry() {

        return country;
    }


    public void setCountry(
            String country) {

        this.country = country;
    }
}
