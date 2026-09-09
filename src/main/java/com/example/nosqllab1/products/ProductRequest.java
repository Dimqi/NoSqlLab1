package com.example.nosqllab1.products;

import java.math.BigDecimal;

public record ProductRequest(String name, String description, BigDecimal price) {
}
