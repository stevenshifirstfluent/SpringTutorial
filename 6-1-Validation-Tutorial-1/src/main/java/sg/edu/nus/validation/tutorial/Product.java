package sg.edu.nus.validation.tutorial;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;


@Entity
public class Product {


    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "system-uuid"
    )
    @GenericGenerator(
            name = "system-uuid",
            strategy = "uuid2"
    )
    private String id;


    @NotEmpty(
            message = "Name is required"
    )
    @Size(
            min = 2,
            max = 50,
            message =
                    "Name must be between 2 and 50 characters"
    )
    private String name;


    @Length(
            min = 3,
            message =
                    "Type must be at least 3 characters"
    )
    private String type;


    @NotBlank(
            message = "Category is required"
    )
    private String category;


    @DecimalMin(
            value = "0.01",
            message =
                    "Price must be greater than zero"
    )
    private BigDecimal price;


    @Min(
            value = 0,
            message =
                    "Stock quantity cannot be negative"
    )
    private int stockQuantity;


    @Size(
            max = 500,
            message =
                    "Description must not exceed 500 characters"
    )
    private String description;


    public Product() {
    }


    public String getId() {

        return id;
    }


    public void setId(
            String id) {

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


    public String getCategory() {

        return category;
    }


    public void setCategory(
            String category) {

        this.category = category;
    }


    public BigDecimal getPrice() {

        return price;
    }


    public void setPrice(
            BigDecimal price) {

        this.price = price;
    }


    public int getStockQuantity() {

        return stockQuantity;
    }


    public void setStockQuantity(
            int stockQuantity) {

        this.stockQuantity =
                stockQuantity;
    }


    public String getDescription() {

        return description;
    }


    public void setDescription(
            String description) {

        this.description =
                description;
    }
}