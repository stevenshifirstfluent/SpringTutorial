package sg.edu.nus.restful.session1.dto;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        BigDecimal price,
        Integer stockQuantity,
        Long categoryId,
        String brand,
        boolean active
) {
}
