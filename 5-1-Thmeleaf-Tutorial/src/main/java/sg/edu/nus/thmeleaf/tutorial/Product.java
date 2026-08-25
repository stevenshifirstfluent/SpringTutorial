package sg.edu.nus.thmeleaf.tutorial;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Product {

    private Long id;

    private String name;

    private String description;

    private String type;

    private String category;

    private BigDecimal price;

    private boolean inStock;

    private List<String> comments = new ArrayList<>();


    public Product() {
    }


    public Product(
            Long id,
            String name,
            String description,
            String type,
            String category,
            BigDecimal price,
            boolean inStock) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.category = category;
        this.price = price;
        this.inStock = inStock;
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


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public String getCategory() {
        return category;
    }


    public void setCategory(String category) {
        this.category = category;
    }


    public BigDecimal getPrice() {
        return price;
    }


    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    public boolean isInStock() {
        return inStock;
    }


    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }


    public List<String> getComments() {
        return comments;
    }


    public void setComments(List<String> comments) {
        this.comments = comments;
    }
}
