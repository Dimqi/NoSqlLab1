package com.example.nosqllab1.favorites;

import com.example.nosqllab1.products.ProductResponse;
import com.example.nosqllab1.products.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Service
public class FavoriteService {
    private final ProductService productService;
    private Map<Long, Set<Long>> favorites = new ConcurrentHashMap<>();

    public List<ProductResponse> getUsersFavorites(Long userId) {
        Set<Long> favoriteIds = favorites.getOrDefault(userId,  new HashSet<>());
        return favoriteIds.stream().map(productService::getProductById).toList();
        // тут тоже все из бд
    }

    public void addFavorite(Long userId, Long productId) {
        ProductResponse productResponse = productService.getProductById(productId);
        favorites.computeIfAbsent(userId, k -> new HashSet<>());
        favorites.get(userId).add(productResponse.id());
        // тут в бд писать надол
    }

    public void deleteFavorite(Long userId, Long productId) {
        Set<Long> favoriteIds = favorites.get(userId);
        if (favoriteIds != null) {
            favoriteIds.remove(productId);
        }
    }
}
