package com.example.nosqllab1.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class User {

    @Setter
    @Getter
    private String id;

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    private String email;

    @Setter
    @Getter
    private String password;

}
