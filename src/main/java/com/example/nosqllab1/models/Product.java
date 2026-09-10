package com.example.nosqllab1.models;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class Product {

    @Setter
    @Getter
    private String id;

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    private BigDecimal price;

    @Setter
    @Getter
    private String description;

}
