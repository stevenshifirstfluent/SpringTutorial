package sg.edu.nus.session.tutorial;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Pet {

    private Long id;

    @NotBlank(
            message = "Pet name is required"
    )
    @Size(
            min = 2,
            max = 30,
            message = "Pet name must be between 2 and 30 characters"
    )
    private String name;

    @NotBlank(
            message = "Pet type is required"
    )
    private String type;

    @Min(
            value = 0,
            message = "Age cannot be negative"
    )
    private int age;


    public Pet() {
    }


    public Pet(
            Long id,
            String name,
            String type,
            int age) {

        this.id = id;
        this.name = name;
        this.type = type;
        this.age = age;
    }


    public Long getId() {

        return id;
    }


    public void setId(
            Long id) {

        this.id = id;
    }


    public String getName() {

        return name;
    }


    public void setName(
            String name) {

        this.name = name;
    }


    public String getType() {

        return type;
    }


    public void setType(
            String type) {

        this.type = type;
    }


    public int getAge() {

        return age;
    }


    public void setAge(
            int age) {

        this.age = age;
    }
}