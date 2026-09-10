package com.example.nosqllab1.products;

import com.example.nosqllab1.models.Product;

import java.math.BigDecimal;

public record ProductResponse(Long id, String name, String description, BigDecimal price) {
    public static ProductResponse fromEntity(Product product) {
        if (product == null) {
            return null;
        }

        Long parsedId = null;
        if (product.getId() != null) {
            try {
                parsedId = Long.parseLong(product.getId());
            } catch (NumberFormatException e) {
                parsedId = null;
            }
        }

        return new ProductResponse(
                parsedId,
                product.getName(),
                product.getDescription(),
                product.getPrice()
        );
    }
}
