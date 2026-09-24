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
        OperationLog operationLog = new OperationLog(
                UUID.randomUUID().toString(),
                userId,
                operation,
                LocalDateTime.now().toString()
        );

        operationRepository.save(operationLog);

        String cacheKey = String.valueOf(userId);
        Optional<OperationCache> cacheOperations = operationCacheRepository.findById(cacheKey);

        if (cacheOperations.isPresent()) {
            OperationCache cache = cacheOperations.get();
            List<OperationLog> operations = new ArrayList<>(cache.operations() != null ? cache.operations() : Collections.emptyList());
            operations.addFirst(operationLog);
            if (operations.size() > 10) {
                operations.removeLast();
            }
            operationCacheRepository.save(new OperationCache(cacheKey, operations));
            log.info("Cache update for user {}", userId);
        }
    }

    public List<OperationLog> getUserOperations(Long userId) {
        String cacheKey = String.valueOf(userId);

        Optional<OperationCache> cache = operationCacheRepository.findById(cacheKey);
        if (cache.isPresent()) {
            log.info("Cache hit for user {}", userId);
            return cache.get().operations();
        }

        log.info("Cache miss for user {}", userId);
        List<OperationLog> userOps = operationRepository.findAll()
                .stream()
                .filter(op -> Objects.equals(op.userId(), userId))
                .sorted(Comparator.comparing(OperationLog::operationTime).reversed())
                .limit(10)
                .collect(Collectors.toCollection(ArrayList::new));

        operationCacheRepository.save(new OperationCache(cacheKey, userOps));

        return userOps;
    }
}