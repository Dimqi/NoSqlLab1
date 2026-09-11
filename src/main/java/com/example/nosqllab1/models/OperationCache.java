package com.example.nosqllab1.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OperationCache {
    String userId;
    List<OperationLog> operations;
}
