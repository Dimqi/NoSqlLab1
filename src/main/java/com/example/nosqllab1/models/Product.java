package com.example.nosqllab1.models;


import lombok.Getter;
import lombok.Setter;

public class Product {
    @Setter
    @Getter
    private String id;

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    private double price;

    @Setter
    @Getter
    private String description;

}
