package com.example.nosqllab1.models;

public record OperationLog(
        String id,
        Long userId,
        String operation,
        String operationTime
) {}