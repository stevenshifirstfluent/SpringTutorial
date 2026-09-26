package sg.edu.nus.restful.session1.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record ProductPatchRequest(
        @Size(min = 1, max = 120, message = "Name must contain 1 to 120 characters")
        String name,

        @DecimalMin(value = "0.01", message = "Price must be positive")
        BigDecimal price,

        @Min(value = 0, message = "Stock quantity cannot be negative")
        Integer stockQuantity,

        Long categoryId,

        @Size(max = 80, message = "Brand must not exceed 80 characters")
        String brand
) {
}
