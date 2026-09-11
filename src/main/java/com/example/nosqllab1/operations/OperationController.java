package com.example.nosqllab1.operations;

import com.example.nosqllab1.models.OperationLog;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/operations")
@RestController
public class OperationController {
    private final OperationService operationService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<OperationLog>> getOperations(@PathVariable Long userId) {
        List<OperationLog> operations = operationService.getUserOperations(userId);
        return ResponseEntity.ok(operations);
    }
}
