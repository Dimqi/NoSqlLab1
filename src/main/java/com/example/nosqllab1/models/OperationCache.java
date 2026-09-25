package com.example.nosqllab1.models;

import java.util.List;

public record OperationCache(
        String userId,
        List<OperationLog> operations
) {}