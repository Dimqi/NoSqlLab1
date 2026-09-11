package com.example.nosqllab1.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OperationLog {
    String id;
    Long userId;
    String operation;
    String operationTime;
}
