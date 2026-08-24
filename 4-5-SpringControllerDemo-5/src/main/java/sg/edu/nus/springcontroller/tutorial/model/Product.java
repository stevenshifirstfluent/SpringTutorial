package sg.edu.nus.springcontroller.tutorial.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/*
 * Marks this class as a JPA entity.
 *
 * Spring Data JPA will map this class
 * to a database table.
 */
@Entity
public class Product {

    /*
     * Primary key of the Product entity.
     *
     * GenerationType.IDENTITY means the
     * database generates the ID value.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String brand;

    private String description;

    private double price;

    /*
     * Specifies the date format expected
     * when Spring binds form data to this field.
     *
     * Example:
     * 24-08-2026
     */
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dom;


    /*
     * JPA requires a no-argument constructor.
     */
    public Product() {
    }


    /*
     * Convenience constructor used when
     * creating Product objects manually.
     */
    public Product(
            String name,
            String brand,
            String description,
            double price,
            LocalDate dom) {

        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.dom = dom;
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getBrand() {
        return brand;
    }


    public void setBrand(String brand) {
        this.brand = brand;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public double getPrice() {
        return price;
    }


    public void setPrice(double price) {
        this.price = price;
    }


    public LocalDate getDom() {
        return dom;
    }


    public void setDom(LocalDate dom) {
        this.dom = dom;
    }
}
