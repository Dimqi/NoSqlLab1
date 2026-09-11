package com.example.nosqllab1.models;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Product {
    private String id;
    private String name;
    private BigDecimal price;
    private String description;
}
