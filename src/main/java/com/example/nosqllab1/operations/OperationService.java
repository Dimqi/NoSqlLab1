package com.example.nosqllab1.operations;

import com.example.nosqllab1.models.OperationCache;
import com.example.nosqllab1.models.OperationLog;
import com.example.nosqllab1.repository.OperationCacheRepository;
import com.example.nosqllab1.repository.OperationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class OperationService {
    private final OperationRepository operationRepository;
    private final OperationCacheRepository operationCacheRepository;

    public void logOperation(Long userId, String operation) {
        OperationLog operationLog = new OperationLog();
        operationLog.setId(UUID.randomUUID().toString());
        operationLog.setUserId(userId);
        operationLog.setOperation(operation);
        operationLog.setOperationTime(LocalDateTime.now().toString());

        operationRepository.save(operationLog);
        operationCacheRepository.delete(String.valueOf(userId));
    }

    public List<OperationLog> getUserOperations(Long userId) {
        Optional<OperationCache> operations = operationCacheRepository.findById(String.valueOf(userId));
        if (operations.isPresent()) {
            log.info("cache hit");
            return operations.get().getOperations();
        }
        log.info("cache miss");
        List<OperationLog> op = operationRepository.findAll()
                .stream()
                .filter(operation -> operation.getUserId().equals(userId))
                .sorted(Comparator.comparing(OperationLog::getOperationTime).reversed())
                .limit(10L).collect(Collectors.toCollection(ArrayList::new));
        OperationCache operationCache = new OperationCache();
        operationCache.setOperations(op);
        operationCache.setUserId(String.valueOf(userId));
        operationCacheRepository.save(operationCache);
        return op;
    }
}
